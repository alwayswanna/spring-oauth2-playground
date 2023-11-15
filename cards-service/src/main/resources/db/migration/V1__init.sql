CREATE TABLE user_card
(
    id                      uuid                            NOT NULL PRIMARY KEY,
    card_title              text                            NOT NULL,
    description             text,
    user_id                 uuid                            NOT NULL,
    last_modified_date       timestamp without time zone     NOT NULL
);

CREATE INDEX ON user_card(card_title);
CREATE INDEX ON user_card(user_id);