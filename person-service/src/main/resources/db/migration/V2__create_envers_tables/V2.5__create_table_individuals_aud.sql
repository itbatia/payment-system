CREATE TABLE person.individuals_aud
(
    id              UUID     NOT NULL,
    rev             BIGINT   NOT NULL REFERENCES person.revinfo (rev),
    revtype         SMALLINT NOT NULL,
    user_id         UUID,
    passport_number VARCHAR(32),
    phone_number    VARCHAR(32),
    status          VARCHAR(32),
    updated_at      TIMESTAMPTZ,
    PRIMARY KEY (id, rev)
);
comment on column person.individuals_aud.id is 'Unique individual identifier (UUID)';
comment on column person.individuals_aud.rev is 'Unique revision ID';
comment on column person.individuals_aud.revtype is 'Revision type: 0 = ADD, 1 = MOD, 2 = DEL';
comment on column person.individuals_aud.user_id is 'Unique user identifier (UUID)';
comment on column person.individuals_aud.passport_number is 'Passport number';
comment on column person.individuals_aud.phone_number is 'Phone number in international format';
comment on column person.individuals_aud.status is 'Entry status: ACTIVE/DELETED';
comment on column person.individuals_aud.updated_at is 'Last update timestamp with timezone (ISO 8601)';
