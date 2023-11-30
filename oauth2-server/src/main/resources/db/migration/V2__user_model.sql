CREATE TABLE accounts_user
(
    id              uuid                PRIMARY KEY,
    username        varchar(30)         NOT NULL,
    password        varchar(250)        NOT NULL,
    first_name      varchar(30)         NOT NULL,
    middle_name     varchar(30)         NOT NULL,
    last_name       varchar(30),
    email           varchar(30)         NOT NULL,
    birth_date      date                NOT NULL,
    disabled        boolean             NOT NULL
);

CREATE INDEX ON accounts_user(username);

CREATE TABLE account_role
(
    id            uuid           PRIMARY KEY,
    role_name     varchar(50)    NOT NULL UNIQUE
);

CREATE TABLE account_to_role
(
    account_id    uuid           NOT NULL,
    role_id       uuid           NOT NULL
);