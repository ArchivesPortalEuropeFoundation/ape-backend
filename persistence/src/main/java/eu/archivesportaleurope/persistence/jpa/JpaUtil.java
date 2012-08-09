package eu.archivesportaleurope.persistence.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import eu.apenet.persistence.exception.PersistenceException;

/**
 * Basic Hibernate utility class
 * 
 * @author bverhoef
 *
 */
public class JpaUtil {

    private static final Logger log = Logger.getLogger(JpaUtil.class);

	private static EntityManagerFactory entityManagerFactory;
	
	private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();

	private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<EntityTransaction>();

	

	public static void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("ape-database");
	}



	
	public static void closeEntityManagerFactory(){
		if (entityManagerFactory != null){
			entityManagerFactory.close();
		}
	}

	/**
	 * Retrieves the current Session local to the thread.
	 * <p/>
	 * If no Session is open, opens a new Session for the running thread.
	 * 
	 * @return Session
	 */
	public static EntityManager getEntityManager() {
		EntityManager entityManager = threadEntityManager.get();
		if (entityManager == null) {
			if (log.isDebugEnabled()) {
				log.debug("Opening new Session for this thread.");
			}
			entityManager = entityManagerFactory.createEntityManager();
			threadEntityManager.set(entityManager);
		}

		return entityManager;
	}

	/**
	 * Closes the Session local to the thread.
	 */
	public static void closeDatabaseSession() {
		EntityManager entityManager = threadEntityManager.get();
		threadEntityManager.remove();
		if (entityManager != null && entityManager.isOpen()) {
			if (log.isDebugEnabled()) {
				log.debug("Closing Session of this thread.");
			}
			entityManager.close();
		}
	}


	/**
	 * Start a new database transaction.
	 */
	public static void beginDatabaseTransaction() {
		EntityTransaction tx = threadTransaction.get();

		if (tx == null) {
			if (log.isDebugEnabled()) {
				log.debug("Starting new database transaction in this thread.");
			}
			tx = getEntityManager().getTransaction();
			threadTransaction.set(tx);
			tx.begin();
		}

	}

	/**
	 * Commit the database transaction.
	 */
	public static void commitDatabaseTransaction() {
		EntityTransaction tx = threadTransaction.get();
		try {
			if (tx != null &&  !tx.getRollbackOnly()) {
				if (log.isDebugEnabled()) {
					log.debug("Committing database transaction of this thread.");
				}
				tx.commit();
			}
			threadTransaction.remove();
		}catch (Exception ex) {
			log.error(ex);
			rollbackDatabaseTransaction();
			throw new PersistenceException(ex);
		}
	}

	/**
	 * rollback the database transaction.
	 */
	public static void rollbackDatabaseTransaction() {
		EntityTransaction tx = threadTransaction.get();
		threadTransaction.remove();
		if (tx != null) {
			if (log.isDebugEnabled()) {
				log.debug("Trying to rollback database transaction of this thread.");
			}
			tx.rollback();
		}
	}

	public static boolean entityManagerClosed(){
		return threadEntityManager.get() == null;
	}
	public static boolean noTransaction(){
		return threadTransaction.get() == null;
	}

}
