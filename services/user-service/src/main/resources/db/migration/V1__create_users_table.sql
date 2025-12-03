create table if not exists users(
    id uuid primary key not null default gen_random_uuid(),
    username varchar(20) not null unique ,
    password varchar(500) not null ,
    email varchar(50) not null unique ,
    role varchar(20) not null ,
    created_at timestamp default current_timestamp,
    active boolean not null default true
);