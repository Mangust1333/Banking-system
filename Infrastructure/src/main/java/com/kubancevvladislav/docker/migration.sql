CREATE TYPE gender_entity AS ENUM (
    'MALE',
    'FEMALE'
);

CREATE TYPE hair_color_entity AS ENUM (
    'White',
    'Black',
    'Red',
    'Yellow',
    'Orange',
    'Green',
    'Blue',
    'Purple',
    'Pink',
    'Brown',
    'Grey'
);

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE accounts_id_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 1;

CREATE SEQUENCE transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 1;

CREATE TABLE users (
    id NUMERIC(38, 0) PRIMARY KEY DEFAULT nextval('users_id_seq'),
    login VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    age SMALLINT NOT NULL,
    gender gender_entity NOT NULL,
    hair_color hair_color_entity NOT NULL,
    CONSTRAINT age_non_negative CHECK (age >= 0),
    CONSTRAINT login_non_empty CHECK (login <> '')
);

CREATE TABLE accounts (
    id NUMERIC(38, 0) PRIMARY KEY DEFAULT nextval('accounts_id_seq'),
    user_login VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    CONSTRAINT balance_non_negative CHECK (balance >= 0),
    CONSTRAINT fk_user_login FOREIGN KEY (user_login) REFERENCES users(login) ON DELETE CASCADE
);

CREATE TABLE transactions (
    transaction_id NUMERIC(38, 0) PRIMARY KEY DEFAULT nextval('transactions_id_seq'),
    amount DECIMAL(19, 2) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE deposit_transactions (
    transaction_id NUMERIC(38, 0) PRIMARY KEY,
    account_id NUMERIC(38, 0) NOT NULL,
    CONSTRAINT fk_deposit_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE,
    CONSTRAINT fk_deposit_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE withdraw_transactions (
    transaction_id NUMERIC(38, 0) PRIMARY KEY,
    account_id NUMERIC(38, 0) NOT NULL,
    CONSTRAINT fk_withdraw_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE,
    CONSTRAINT fk_withdraw_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);


CREATE TABLE transfer_transactions (
    transaction_id NUMERIC(38, 0) PRIMARY KEY,
    sender_account_id NUMERIC(38, 0) NOT NULL,
    receiver_account_id NUMERIC(38, 0) NOT NULL,
    commission DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_transfer_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE,
    CONSTRAINT fk_sender_account FOREIGN KEY (sender_account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    CONSTRAINT fk_receiver_account FOREIGN KEY (receiver_account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE user_friends (
    user_id NUMERIC(38, 0) NOT NULL,
    friend_id NUMERIC(38, 0) NOT NULL,
    user_login VARCHAR(255) NOT NULL,
    friend_login VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_friend FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_login FOREIGN KEY (user_login) REFERENCES users(login) ON DELETE CASCADE,
    CONSTRAINT fk_friend_login FOREIGN KEY (friend_login) REFERENCES users(login) ON DELETE CASCADE,
    CONSTRAINT no_self_friendship CHECK (user_id <> friend_id)
);
