CREATE TABLE IF NOT EXISTS customer
(
    id UUID DEFAULT gen_random_uuid(),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS currency
(
    id            UUID DEFAULT gen_random_uuid(),
    currency_code VARCHAR(3) NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS account
(
    id           UUID DEFAULT gen_random_uuid(),
    customer_id  UUID NOT NULL,
    country_code VARCHAR(3),

    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE IF NOT EXISTS transaction
(
    id          UUID DEFAULT gen_random_uuid(),
    account_id  UUID           NOT NULL,
    amount      NUMERIC(19, 4) NOT NULL,
    currency_id UUID           NOT NULL,
    direction   VARCHAR(3)     NOT NULL,
    description VARCHAR(140)   NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account (id),
    FOREIGN KEY (currency_id) REFERENCES currency (id)
);

CREATE TABLE IF NOT EXISTS balance
(
    id          UUID DEFAULT gen_random_uuid(),
    account_id  UUID           NOT NULL,
    amount      NUMERIC(19, 4) NOT NULL,
    currency_id UUID           NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account (id),
    FOREIGN KEY (currency_id) REFERENCES currency (id)
);
