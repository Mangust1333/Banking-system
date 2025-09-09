CREATE TABLE account_event_entity (
    id SERIAL PRIMARY KEY,
    account_id BIGINT,
    payload TEXT,
    timestamp TIMESTAMP
);

CREATE TABLE client_event_entity (
    id SERIAL PRIMARY KEY,
    client_login VARCHAR(255),
    payload TEXT,
    timestamp TIMESTAMP
);
