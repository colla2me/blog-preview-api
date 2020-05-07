create table USER (
    id bigint primary key auto_increment,
    username varchar(100) unique not null,
    encrypted_password varchar(100) not null,
    avatar varchar(100),
    updated_at datetime default now(),
    created_at datetime default now()
);