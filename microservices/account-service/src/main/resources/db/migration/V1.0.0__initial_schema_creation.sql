CREATE TABLE accounts (
    `id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR (50) NOT NULL,
    `username` VARCHAR (50) NOT NULL UNIQUE,
    `password` VARCHAR (50) NOT NULL
)