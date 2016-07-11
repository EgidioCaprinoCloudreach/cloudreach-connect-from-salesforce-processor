CREATE SCHEMA IF NOT EXISTS __SCHEMA__;

SET SCHEMA '__SCHEMA__';

CREATE TABLE IF NOT EXISTS from_salesforce_record (
  id_from_salesforce_record BIGSERIAL NOT NULL PRIMARY KEY,
  salesforce_object         VARCHAR   NOT NULL,
  salesforce_id             VARCHAR   NOT NULL UNIQUE,
  data                      JSON      NOT NULL,
  processed                 BOOLEAN   NOT NULL,
  processed_date            TIMESTAMP,
  insert_date               TIMESTAMP NOT NULL,
  last_error                TEXT,
  priority                  BIGINT    NOT NULL
);
