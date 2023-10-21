CREATE TABLE person (
    id          serial          PRIMARY KEY     NOT NULL,
    login       varchar(2000),
    password    varchar(2000)
);