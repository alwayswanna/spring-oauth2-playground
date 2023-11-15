CREATE TABLE accounts_user
(
    id          uuid             NOT NULL PRIMARY KEY,
    username    varchar(30)      NOT NULL,
    first_name  varchar(30)       NOT NULL,
    middle_name varchar(30)      NOT NULL,
    last_name   varchar(30),
    email       varchar(30)      NOT NULL,
    birth_date  date             NOT NULL,
    disabled     boolean         NOT NULL
);

CREATE INDEX ON accounts_user(username);

CREATE TABLE account_role
(
    id            uuid           NOT NULL PRIMARY KEY,
    role_name     varchar(50)    NOT NULL
);

CREATE TABLE account_to_role
(
    account_id    uuid           NOT NULL,
    role_id       uuid           NOT NULL
);

CREATE TABLE keycloak_authorization_session
(
    id                     uuid                        NOT NULL PRIMARY KEY,
    session_state          text                        NOT NULL,
    scopes                 text                        NOT NULL,
    access_token_expire_in timestamp without time zone NOT NULL,
    username               text                        NOT NULL,
    create_at              timestamp without time zone NOT NULL
);
