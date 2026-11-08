-- liquibase formatted sql

-- changeset istad:005-1
ALTER TABLE permissions
    ALTER COLUMN resource SET NOT NULL;

-- changeset istad:005-2
ALTER TABLE permissions
    ALTER COLUMN action SET NOT NULL;
