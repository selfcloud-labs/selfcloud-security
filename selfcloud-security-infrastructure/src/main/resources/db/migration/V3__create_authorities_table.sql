CREATE TABLE IF NOT EXISTS security.authorities (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL UNIQUE,
    description     VARCHAR(255)
);