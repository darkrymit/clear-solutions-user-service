DROP SEQUENCE IF EXISTS users_id_seq;

CREATE SEQUENCE IF NOT EXISTS users_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE users
(
    id               BIGINT       NOT NULL       DEFAULT NEXTVAL('users_id_seq'),
    email            VARCHAR(255) NOT NULL,
    first_name       VARCHAR(50)  NOT NULL,
    last_name        VARCHAR(50)  NOT NULL,
    birth_date       date         NOT NULL,
    address          VARCHAR(255),
    phone_number     VARCHAR(15),
    created_at       TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
    last_modified_at TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);