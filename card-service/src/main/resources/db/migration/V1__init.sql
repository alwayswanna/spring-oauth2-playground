CREATE TABLE user_card
(
    id                      uuid                            PRIMARY KEY,
    card_title              text                            NOT NULL,
    description             text,
    sub                     text                            NOT NULL,
    last_modified_date      timestamp without time zone     NOT NULL
);

CREATE INDEX ON user_card(card_title);
CREATE INDEX ON user_card(sub);