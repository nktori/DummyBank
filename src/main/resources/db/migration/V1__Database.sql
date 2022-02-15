CREATE TABLE IF NOT EXISTS Account
(
    id      SERIAL,
    balance INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Transaction
(
    id        SERIAL,
    payer_Id  INT       NOT NULL REFERENCES Account (id),
    payee_Id  INT       NOT NULL REFERENCES Account (id),
    amount    INT       NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
