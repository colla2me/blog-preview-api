create table BLOG
(
    id                 bigint primary key auto_increment,
    user_id            bigint,
    title              varchar(128) unique not null,
    content            TEXT,
    description        varchar(256),
    updated_at         datetime default now(),
    created_at         datetime default now()
);