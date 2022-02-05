CREATE TABLE accounts (
    `id` BIGINT(20) AUTO_INCREMENT,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR (50) NOT NULL,
    `username` VARCHAR (50) NOT NULL ,
    `password` VARCHAR (50) NOT NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `uc_username` UNIQUE (`username`)
);