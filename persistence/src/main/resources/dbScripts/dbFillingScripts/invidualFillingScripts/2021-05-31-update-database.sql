CREATE SEQUENCE rights_information_id_seq
START WITH 100
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
ALTER TABLE rights_information_id_seq
  OWNER TO postgres;
GRANT ALL ON SEQUENCE rights_information_id_seq TO postgres;
GRANT ALL ON SEQUENCE rights_information_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE rights_information_id_seq TO admin;

CREATE TABLE rights_information
(
  id integer NOT NULL DEFAULT nextval('rights_information_id_seq'::regclass),
  abbreviation character varying(20) NOT NULL,
  rights_link character varying(255) NOT NULL,
  description text,
  rights_name character varying(100),
  CONSTRAINT rights_information_pkey PRIMARY KEY (id)
);
ALTER TABLE rights_information
  OWNER TO postgres;
GRANT ALL ON TABLE rights_information TO postgres;
GRANT ALL ON TABLE rights_information TO apenet_dashboard;
GRANT ALL ON TABLE rights_information TO admin;

ALTER TABLE archival_institution ADD COLUMN rights_information integer;
ALTER TABLE archival_institution ADD CONSTRAINT archival_institution_rinf_id_fkey FOREIGN KEY (rights_information)
      REFERENCES rights_information (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE archival_institution ADD COLUMN share_with_wikimedia boolean;
ALTER TABLE archival_institution ADD COLUMN rights_holder character varying(255);

ALTER TABLE c_level ADD COLUMN rights_information integer;
ALTER TABLE c_level
  ADD CONSTRAINT c_level_rinf_id_fkey FOREIGN KEY (rights_information)
      REFERENCES c_level (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE eac_cpf ADD COLUMN rights_information integer;
ALTER TABLE eac_cpf
  ADD CONSTRAINT eac_cpf_rinf_id_fkey FOREIGN KEY (rights_information)
      REFERENCES eac_cpf (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE finding_aid ADD COLUMN rights_information integer;
ALTER TABLE finding_aid
  ADD CONSTRAINT finding_aid_rinf_id_fkey FOREIGN KEY (rights_information)
      REFERENCES rights_information (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE holdings_guide ADD COLUMN rights_information integer;
ALTER TABLE holdings_guide
  ADD CONSTRAINT holdings_guide_rinf_id_fkey FOREIGN KEY (rights_information)
      REFERENCES holdings_guide (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE source_guide ADD COLUMN rights_information integer;
ALTER TABLE source_guide
  ADD CONSTRAINT source_guide_rinf_id_fkey FOREIGN KEY (rights_information)
      REFERENCES source_guide (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;