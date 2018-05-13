CREATE TABLE Users (
  PRIMARY KEY (id),
  id       INT          NOT NULL UNIQUE,
  username VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO Users (id, username)
VALUES (1, 'Johanne');