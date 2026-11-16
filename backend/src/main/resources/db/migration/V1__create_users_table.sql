CREATE TABLE users (
                       id            VARCHAR(255) PRIMARY KEY,
                       fio           VARCHAR(50)  NOT NULL,
                       phone_number  VARCHAR(11)  UNIQUE,
                       password      VARCHAR(255) NOT NULL,
                       email         VARCHAR(30)  NOT NULL UNIQUE,
                       age           INTEGER,
                       gender        VARCHAR(10),
                       created_at    TIMESTAMP,
                       updated_at    TIMESTAMP
);