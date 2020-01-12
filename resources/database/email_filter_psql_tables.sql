create table if not exists core_label_metadata(
id serial primary key,
name varchar(255) NOT NULL,
creation_time timestamp NOT NULL
);

create table if not exists core_user_profile(
id serial primary key,
user_email_id varchar(255) NOT NULL,
label_id int references core_label_metadata on delete cascade on update cascade
);

create table if not exists label_filter(
id serial primary key,
filter_name varchar(255),
label_id int references core_label_metadata on delete cascade on update cascade,
is_email_id_filter boolean,
email_id_keywords bytea,
is_subject_filter boolean,
subject_keywords bytea,
is_body_filter boolean,
body_keywords bytea,
creation_date timestamp
);

create table if not exists delete_filter(
id serial primary key,
filter_name varchar(255),
label_id int references core_label_metadata on delete cascade on update cascade,
is_email_id_filter boolean,
email_id_keywords bytea,
is_subject_filter boolean,
subject_keywords bytea,
is_body_filter boolean,
body_keywords bytea,
creation_date timestamp
);

create table if not exists core_run_manager(
id serial primary key,
label_filter_id int references label_filter on delete cascade on update cascade,
delete_filter_id int references delete_filter on delete cascade on update cascade,
run_name varchar(255),
status int,
failure_reason varchar(1024)
);