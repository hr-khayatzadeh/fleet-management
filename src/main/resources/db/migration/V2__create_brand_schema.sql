CREATE TABLE brand
(
    id              UUID         NOT NULL,
    name            VARCHAR(255) NOT NULL,
    manufacturer    VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP,
    last_updated_at TIMESTAMP,
    version         INT,
    CONSTRAINT pk_brand PRIMARY KEY (id)
);