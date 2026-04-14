-- Таблица ревизий
CREATE TABLE person.revinfo
(
    rev         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    revtstmp    BIGINT NOT NULL,
    modified_by TEXT   NOT NULL DEFAULT 'unknown'
);
comment on column person.revinfo.rev is 'Unique revision ID (auto-generated)';
comment on column person.revinfo.revtstmp is 'Timestamp of revision in milliseconds since epoch (UTC)';
comment on column person.revinfo.modified_by is 'Identifier of the user or system that triggered this revision (e.g., UUID, username, etc.)';
