CREATE TABLE person.users
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    address_id UUID REFERENCES person.addresses (id),
    secret_key VARCHAR(32),
    email      VARCHAR(1024) NOT NULL UNIQUE,
    first_name VARCHAR(32),
    last_name  VARCHAR(32),
    filled     BOOLEAN,
    created_at TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ      DEFAULT NULL
);
comment on column person.users.id is 'Unique user identifier (UUID)';
comment on column person.users.address_id is 'Unique address identifier (UUID)';
comment on column person.users.secret_key is 'Secret key for internal authentication';
comment on column person.users.email is 'Unique email address';
comment on column person.users.first_name is 'First name of the user';
comment on column person.users.last_name is 'Last name of the user';
comment on column person.users.filled is 'Are all user details filled in';
comment on column person.users.created_at is 'Creation timestamp with timezone (ISO 8601)';
comment on column person.users.updated_at is 'Last update timestamp with timezone (ISO 8601)';
