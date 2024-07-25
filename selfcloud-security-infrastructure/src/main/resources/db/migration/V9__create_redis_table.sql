SET sql_mode = '';

CREATE TABLE security.tokens (
  id              VARCHAR(255) PRIMARY KEY,
  username        VARCHAR(255),
  token           VARCHAR(511),
  modified_by     VARCHAR(255),
  modified_on     TIMESTAMP,
  created_on      TIMESTAMP NOT NULL,
  created_by      VARCHAR(255)
);
