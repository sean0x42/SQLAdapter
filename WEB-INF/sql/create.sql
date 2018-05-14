-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE Users (
  PRIMARY KEY (id),
  id       CHAR(36)     NOT NULL UNIQUE,
  username VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO Users (id, username)
VALUES ('54947df8-0e9e-4471-a2f9-9af509fb5889', 'Johanne');