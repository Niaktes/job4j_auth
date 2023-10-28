CREATE TABLE person (
    id          serial          PRIMARY KEY     NOT NULL,
    login       varchar(2000)   UNIQUE          NOT NULL,
    password    varchar(2000)                   NOT NULL
);