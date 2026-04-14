CREATE TABLE person.countries_aud
(
    id         BIGINT   NOT NULL,
    rev        BIGINT   NOT NULL REFERENCES person.revinfo (rev),
    revtype    SMALLINT NOT NULL,
    name       VARCHAR(32),
    alpha2     VARCHAR(2),
    alpha3     VARCHAR(3),
    status     VARCHAR(32),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id, rev)
);
comment on column person.countries_aud.id is 'Internal unique country ID';
comment on column person.countries_aud.rev is 'Unique revision ID';
comment on column person.countries_aud.revtype is 'Revision type: 0 = ADD, 1 = MOD, 2 = DEL';
comment on column person.countries_aud.name is 'Full country name';
comment on column person.countries_aud.alpha2 is 'ISO 3166-1 alpha-2 code';
comment on column person.countries_aud.alpha3 is 'ISO 3166-1 alpha-3 code';
comment on column person.countries_aud.status is 'Entry status: ACTIVE/DELETED';
comment on column person.countries_aud.updated_at is 'Last update timestamp with timezone (ISO 8601)';
