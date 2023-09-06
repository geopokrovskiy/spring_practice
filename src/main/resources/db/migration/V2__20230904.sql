-- This is a migration file for file table creation

CREATE TABLE if not exists files (
    id INT primary key auto_increment,
    location varchar(256) not null,
    status ENUM('ACTIVE', 'DELETED') NOT NULL
);