CREATE TABLE user_statistics
(
    id          uuid                        PRIMARY KEY,
    sub         text                        NOT NULL,
    card_count  int                         NOT NULL,
    last_update timestamp with time zone    NOT NULL
);

CREATE INDEX ON user_statistics(sub);