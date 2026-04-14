CREATE TABLE person.addresses
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id  BIGINT REFERENCES person.countries (id),
    address     VARCHAR(128),
    zip_code    VARCHAR(32),
    city        VARCHAR(32),
    state       VARCHAR(32),
    archived_at TIMESTAMPTZ      DEFAULT NULL,
    created_at  TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ      DEFAULT NULL
);
comment on column person.addresses.id is 'Unique address identifier (UUID)';
comment on column person.addresses.country_id is 'Internal unique country ID';
comment on column person.addresses.address is 'Street address';
comment on column person.addresses.zip_code is 'Postal code';
comment on column person.addresses.city is 'City name';
comment on column person.addresses.state is 'State or region';
comment on column person.addresses.archived_at is 'Timestamp when address was archived (epoch if active)';
comment on column person.addresses.created_at is 'Creation timestamp with timezone (ISO 8601)';
comment on column person.addresses.updated_at is 'Last update timestamp with timezone (ISO 8601)';
