CREATE TABLE person.individuals
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID UNIQUE REFERENCES person.users (id),
    passport_number VARCHAR(32),
    phone_number    VARCHAR(32),
    status          VARCHAR(32),
    created_at      TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ      DEFAULT NULL
);
comment on column person.individuals.id is 'Unique individual identifier (UUID)';
comment on column person.individuals.user_id is 'Unique user identifier (UUID)';
comment on column person.individuals.passport_number is 'Passport number';
comment on column person.individuals.phone_number is 'Phone number in international format';
comment on column person.individuals.status is 'Entry status: ACTIVE/DELETED';
comment on column person.individuals.created_at is 'Creation timestamp with timezone (ISO 8601)';
comment on column person.individuals.updated_at is 'Last update timestamp with timezone (ISO 8601)';
