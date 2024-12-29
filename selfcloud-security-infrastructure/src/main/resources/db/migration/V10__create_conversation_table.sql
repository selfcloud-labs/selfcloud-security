CREATE TABLE IF NOT EXISTS conversations (
    id               BIGINT PRIMARY KEY,
    conv_id          VARCHAR(255),
    from_user        BIGINT,
    to_user          BIGINT,
    time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    content          VARCHAR(1023),
    delivery_status  VARCHAR(255)
);