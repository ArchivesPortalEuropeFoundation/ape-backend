ALTER TABLE ingestionprofile ADD COLUMN europeana_language_description character varying(5);
ALTER TABLE ingestionprofile ADD COLUMN europeana_language_description_from_file boolean;
ALTER TABLE ingestionprofile ADD COLUMN europeana_language_material_description_same boolean;
ALTER TABLE ingestionprofile RENAME COLUMN europeana_languages TO europeana_languages_material;
ALTER TABLE ingestionprofile RENAME COLUMN europeana_languages_from_file TO europeana_languages_material_from_file;