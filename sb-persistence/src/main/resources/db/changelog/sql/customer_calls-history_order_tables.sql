CREATE TABLE customer
(
    customer_id BIGSERIAL primary key,
    name        VARCHAR(255) DEFAULT NULL,
    number      VARCHAR(20)
);

CREATE TABLE calls_history
(
    call_id          BIGSERIAL PRIMARY KEY,
    customer_id      BIGINT      NOT NULL REFERENCES customer (customer_id),
    type             VARCHAR(50) NOT NULL,
    date             TIMESTAMP   NOT NULL,
    duration_seconds INTEGER     NOT NULL
);

CREATE TABLE "order"
(
    order_id     BIGSERIAL PRIMARY KEY,
    customer_id  BIGINT      NOT NULL REFERENCES customer (customer_id),
    state        VARCHAR(20) NOT NULL,
    updated_date TIMESTAMP
);
