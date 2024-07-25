CREATE TABLE IF NOT EXISTS security.users (
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    email                     VARCHAR(255) NOT NULL UNIQUE,
    password                  VARCHAR(255),
    enabled                   BOOLEAN NOT NULL,
    account_non_expired       BOOLEAN NOT NULL,
    credentials_non_expired   BOOLEAN NOT NULL,
    account_non_locked        BOOLEAN NOT NULL,
    rejection_reason          VARCHAR(255),
    state                     VARCHAR(255)
);
