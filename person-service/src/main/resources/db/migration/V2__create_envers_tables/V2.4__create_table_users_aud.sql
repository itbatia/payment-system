CREATE TABLE person.users_aud
(
    id         UUID     NOT NULL,
    rev        BIGINT   NOT NULL REFERENCES person.revinfo (rev),
    revtype    SMALLINT NOT NULL,
    address_id UUID,
    secret_key VARCHAR(32),
    email      VARCHAR(1024),
    first_name VARCHAR(32),
    last_name  VARCHAR(32),
    filled     BOOLEAN,
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id, rev)
);
comment on column person.users_aud.id is 'Unique user identifier (UUID)';
comment on column person.users_aud.rev is 'Unique revision ID';
comment on column person.users_aud.revtype is 'Revision type: 0 = ADD, 1 = MOD, 2 = DEL';
comment on column person.users_aud.address_id is 'Unique address identifier (UUID)';
comment on column person.users_aud.secret_key is 'Secret key for internal authentication';
comment on column person.users_aud.email is 'Unique email address';
comment on column person.users_aud.first_name is 'First name of the user';
comment on column person.users_aud.last_name is 'Last name of the user';
comment on column person.users_aud.filled is 'Are all user details filled in';
comment on column person.users_aud.updated_at is 'Last update timestamp with timezone (ISO 8601)';
