CREATE TABLE IF NOT EXISTS customer
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS currency
(
    id            SERIAL PRIMARY KEY,
    currency_code VARCHAR(3) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS account
(
    id           SERIAL PRIMARY KEY,
    customer_id  INT NOT NULL,
    country_code VARCHAR(3),

    FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE IF NOT EXISTS transaction
(
    id          SERIAL PRIMARY KEY,
    account_id  INT            NOT NULL,
    amount      NUMERIC(19, 4) NOT NULL,
    currency_id INT            NOT NULL,
    direction   VARCHAR(3)     NOT NULL,
    description VARCHAR(140)   NOT NULL,

    FOREIGN KEY (account_id) REFERENCES account (id),
    FOREIGN KEY (currency_id) REFERENCES currency (id)
);

CREATE TABLE IF NOT EXISTS balance
(
    id          SERIAL PRIMARY KEY,
    account_id  INT NOT NULL,
    amount      NUMERIC(19, 4) DEFAULT 0.00,
    currency_id INT NOT NULL,

    FOREIGN KEY (account_id) REFERENCES account (id),
    FOREIGN KEY (currency_id) REFERENCES currency (id)
);
