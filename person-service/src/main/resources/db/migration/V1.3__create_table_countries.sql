CREATE TABLE person.countries
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name       VARCHAR(32) NOT NULL,
    alpha2     VARCHAR(2)  NOT NULL,
    alpha3     VARCHAR(3)  NOT NULL,
    status     VARCHAR(32),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT NULL
);
comment on column person.countries.id is 'Internal unique country ID';
comment on column person.countries.name is 'Full country name';
comment on column person.countries.alpha2 is 'ISO 3166-1 alpha-2 code';
comment on column person.countries.alpha3 is 'ISO 3166-1 alpha-3 code';
comment on column person.countries.status is 'Entry status: ACTIVE/DELETED';
comment on column person.countries.created_at is 'Creation timestamp with timezone (ISO 8601)';
comment on column person.countries.updated_at is 'Last update timestamp with timezone (ISO 8601)';
