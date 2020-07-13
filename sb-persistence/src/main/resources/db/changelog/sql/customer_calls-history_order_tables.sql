CREATE TABLE calls_history
(
    call_id          BIGSERIAL PRIMARY KEY,
    phone_number     VARCHAR(50) NOT NULL,
    type             VARCHAR(50) NOT NULL,
    date             TIMESTAMP   NOT NULL,
    duration_seconds INTEGER     NOT NULL
);

CREATE TABLE order_state
(
    phone_number VARCHAR(50) NOT NULL PRIMARY KEY,
    state        VARCHAR(20) NOT NULL,
    updated_date TIMESTAMP NOT NULL default now()
);
