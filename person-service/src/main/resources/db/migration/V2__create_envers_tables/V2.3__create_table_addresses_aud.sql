CREATE TABLE person.addresses_aud
(
    id          UUID     NOT NULL,
    rev         BIGINT   NOT NULL REFERENCES person.revinfo (rev),
    revtype     SMALLINT NOT NULL,
    country_id  BIGINT,
    address     VARCHAR(128),
    zip_code    VARCHAR(32),
    city        VARCHAR(32),
    state       VARCHAR(32),
    archived_at TIMESTAMPTZ,
    updated_at  TIMESTAMPTZ,
    PRIMARY KEY (id, rev)
);
comment on column person.addresses_aud.id is 'Unique address identifier (UUID)';
comment on column person.addresses_aud.rev is 'Unique revision ID';
comment on column person.addresses_aud.revtype is 'Revision type: 0 = ADD, 1 = MOD, 2 = DEL';
comment on column person.addresses_aud.country_id is 'Internal unique country ID';
comment on column person.addresses_aud.address is 'Street address';
comment on column person.addresses_aud.zip_code is 'Postal code';
comment on column person.addresses_aud.city is 'City name';
comment on column person.addresses_aud.state is 'State or region';
comment on column person.addresses_aud.archived_at is 'Timestamp when address was archived (epoch if active)';
comment on column person.addresses_aud.updated_at is 'Last update timestamp with timezone (ISO 8601)';
