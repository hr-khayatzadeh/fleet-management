CREATE TABLE operation_city
(
    id              UUID         NOT NULL,
    created_at      TIMESTAMP,
    last_updated_at TIMESTAMP,
    version         INT,
    name            VARCHAR(255) NOT NULL,
    province        VARCHAR(255) NOT NULL,
    postal_code     VARCHAR(5)   NOT NULL,
    status          VARCHAR(255) NOT NULL,
    CONSTRAINT pk_operation_city PRIMARY KEY (id)
);