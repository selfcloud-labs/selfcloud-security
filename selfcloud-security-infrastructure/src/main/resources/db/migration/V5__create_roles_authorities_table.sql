CREATE TABLE IF NOT EXISTS security.roles_authorities (
    role_id                       BIGINT,
    authority_id                  BIGINT,

    PRIMARY KEY (role_id, authority_id),

    FOREIGN KEY (role_id)         REFERENCES security.roles(id),
    FOREIGN KEY (authority_id)    REFERENCES security.authorities(id)
);