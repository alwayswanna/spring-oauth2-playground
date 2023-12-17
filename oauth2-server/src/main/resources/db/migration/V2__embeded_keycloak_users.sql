-- @formatter:off
CREATE TABLE account
(
    id                  uuid        PRIMARY KEY,
    preferred_username  text        NOT NULL UNIQUE,
    disabled            boolean     DEFAULT false
);

CREATE INDEX ON account(preferred_username);

CREATE TABLE account_role
(
    id            uuid           PRIMARY KEY,
    role_name     varchar(50)    NOT NULL
);

CREATE TABLE account_to_role
(
    account_id    uuid           NOT NULL,
    role_id       uuid           NOT NULL
);