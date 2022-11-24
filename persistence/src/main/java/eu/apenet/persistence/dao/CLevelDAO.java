package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.AbstractContent;
import java.util.List;

import eu.apenet.persistence.vo.CLevel;

/**
 *
 * @author Bastiaan Verhoef
 *
 */
public interface CLevelDAO extends GenericDAO<CLevel, Long> {

    public Long countChildCLevels(Long parentCLevelId);

    public List<CLevel> findChildCLevels(Long parentCLevelId, Integer orderId, Integer maxNumberOfItems);

    public Long countTopCLevels(Long eadContentId);

    public CLevel getTopClevelByFileId(Integer fileId, Class<? extends AbstractContent> clazz, int orderId);

    public List<CLevel> getTopClevelsByFileId(Integer fileId, Class<? extends AbstractContent> clazz, int firstResult, int maxResult);

    public List<CLevel> findTopCLevels(Long eadContentId, Integer orderId, Integer maxNumberOfItems);

    public List<CLevel> findTopEad3CLevels(Integer ead3Id, Integer orderId, Integer maxNumberOfItems);

    public List<String> findChildrenLevels(Long parentId);

    @Deprecated
    public List<CLevel> findChilds(Long parentId);

    @Deprecated
    public List<CLevel> findTopCLevels(Long eadContentId);

    public Long countPossibleLinkedCLevels(Integer id, Class<? extends AbstractContent> clazz);

    public List<CLevel> getNotLinkedCLevels(Integer id, Class<? extends AbstractContent> clazz);

    public List<CLevel> getClevelsFromSgOrHg(Integer aiId, String eadid);

    public List<CLevel> getCLevelsNodes(Long eadContentId);

    public Long countCLevels(Class<? extends AbstractContent> clazz, Integer id);

    public List<CLevel> getCLevels(Class<? extends AbstractContent> clazz, Integer id, int pageNumber, int pageSize);

    public List<CLevel> getCLevels(int pageNumber, int pageSize);

    public Long countAllCLevels();

    public List<CLevel> getCLevel(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, String unitid);

    public List<CLevel> getCLevelWithEad3Id(String repositoryCode, String ead3Id, String unitid);

    public CLevel getCLevel(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, Long id);

    public CLevel getTopCLevel(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, Integer orderId);

    public Long getTopCLevelId(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, Integer orderId);

    public Long getChildCLevelId(Long parentId, Integer orderId);

    public CLevel getChildCLevel(Long parentId, Integer orderId);

    public CLevel getCLevelByCid(String repositoryCode, Class<? extends AbstractContent> clazz, String eadid, String cid);

    public CLevel getCLevelByUnitId(String unitId, String repositoryCode, String ead3Id);

    public CLevel getCLevelByUnitId(String unitid, String ead3recordid);
}
