CREATE TABLE IF NOT EXISTS security.users_roles (
    user_id                       BIGINT,
    role_id                       BIGINT,

    PRIMARY KEY (user_id, role_id),

    FOREIGN KEY (user_id)         REFERENCES security.users(id),
    FOREIGN KEY (role_id)         REFERENCES security.roles(id)
);