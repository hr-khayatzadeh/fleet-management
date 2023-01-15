CREATE TABLE car
(
    id                UUID         NOT NULL,
    brand_id          UUID,
    license_plate     VARCHAR(255) NOT NULL,
    operation_city_id UUID,
    status            VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP,
    last_updated_at   TIMESTAMP,
    version           INT,
    CONSTRAINT pk_car PRIMARY KEY (id)
);

ALTER TABLE car
    ADD CONSTRAINT uc_car_license_plate UNIQUE (license_plate);

ALTER TABLE car
    ADD CONSTRAINT FK_CAR_ON_BRAND FOREIGN KEY (brand_id) REFERENCES brand (id);

ALTER TABLE car
    ADD CONSTRAINT FK_CAR_ON_OPERATION_CITY FOREIGN KEY (operation_city_id) REFERENCES operation_city (id);