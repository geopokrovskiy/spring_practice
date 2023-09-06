-- This is a migration file for events table creation (many-to-many files-to-users)

CREATE table if not exists events(
    id INT primary key auto_increment,
    user_id INT references users.id,
    file_id INT references files.id,
    event enum('UPLOADED', 'DOWNLOADED', 'DELETED') not null,
    status ENUM('ACTIVE', 'DELETED') NOT NULL
);