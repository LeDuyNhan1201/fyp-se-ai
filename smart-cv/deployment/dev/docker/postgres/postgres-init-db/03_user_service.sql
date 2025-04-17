\c user_service_db

create table "user" (
    is_deleted boolean,
    created_at timestamp(6) with time zone,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    version bigint,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(200) not null unique,
    password varchar(500) not null,
    created_by varchar(255),
    deleted_by varchar(255),
    id varchar(255) not null,
    updated_by varchar(255),
    primary key (id)
);