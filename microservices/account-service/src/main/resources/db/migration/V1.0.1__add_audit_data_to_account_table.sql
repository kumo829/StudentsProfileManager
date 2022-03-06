ALTER TABLE accounts ADD COLUMN `created_on` timestamp DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE accounts ADD COLUMN `disabled` boolean DEFAULT false;
ALTER TABLE accounts ADD COLUMN `disabled_on` timestamp NULL;

ALTER TABLE accounts ADD COLUMN `deleted` boolean DEFAULT false;
ALTER TABLE accounts ADD COLUMN `deleted_on` timestamp NULL;
