\c job_service_db

create table job (
        from_salary float(53) not null,
        is_deleted boolean,
        to_salary float(53) not null,
        created_at timestamp(6) with time zone,
        deleted_at timestamp(6) with time zone,
        expired_at timestamp(6) with time zone,
        updated_at timestamp(6) with time zone,
        created_by varchar(255),
        deleted_by varchar(255),
        email varchar(255),
        id varchar(255) not null,
        organization_name varchar(255) not null,
        phone varchar(255),
        position varchar(255) not null,
        raw_text TEXT not null,
        updated_by varchar(255),
        educations varchar(255) array,
        experiences varchar(255) array,
        skills varchar(255) array not null,
        primary key (id)
    )