ALTER TABLE archival_institution_oai_pmh DROP CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey;
ALTER TABLE archival_institution_oai_pmh DROP CONSTRAINT archival_institution_oai_pmh_ai_id_fkey;

ALTER TABLE archival_institution_oai_pmh ADD CONSTRAINT archival_institution_oai_pmh_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;
      
ALTER TABLE archival_institution_oai_pmh ADD CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey FOREIGN KEY (profile_id)
      REFERENCES ingestionprofile (id);

ALTER TABLE ingestionprofile DROP CONSTRAINT ingestionprofile_ai_id_fkey;      
ALTER TABLE ingestionprofile ADD CONSTRAINT ingestionprofile_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;
      
ALTER TABLE ai_alternative_name DROP CONSTRAINT archival_institution_ai_id_fkey;    
ALTER TABLE ai_alternative_name ADD CONSTRAINT archival_institution_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;
      
ALTER TABLE archival_institution DROP CONSTRAINT archival_institution_parent_ai_id_fkey;         
ALTER TABLE archival_institution ADD CONSTRAINT archival_institution_parent_ai_id_fkey FOREIGN KEY (parent_ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;