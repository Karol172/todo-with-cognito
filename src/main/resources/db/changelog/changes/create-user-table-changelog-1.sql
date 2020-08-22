-- liquibase formatted sql
-- changeset author:kcymerys

CREATE TABLE COGNITO_USER
(
    ID            BIGINT DEFAULT NEXTVAL('HIBERNATE_SEQUENCE'),
    USERNAME      VARCHAR(64) UNIQUE NOT NULL,
    PRIMARY KEY(ID)
);