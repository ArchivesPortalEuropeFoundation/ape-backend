ALTER TABLE dashboard_user ADD COLUMN contact_forms_as_cm boolean;
UPDATE dashboard_user set contact_forms_as_cm=false;
