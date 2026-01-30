create table if not exists tasks(
    id uuid primary key unique not null default gen_random_uuid() ,
    assignee_id uuid not null ,
    title varchar(50) not null ,
    description varchar(500) not null ,
    status varchar(50) not null ,
    priority varchar(50) not null ,
    created_at timestamp not null default now() ,
    updated_at timestamp not null default now(),
    due_date timestamp not null default (now() + interval '3 days')
);