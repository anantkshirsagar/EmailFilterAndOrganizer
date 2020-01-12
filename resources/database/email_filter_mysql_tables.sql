#create database if not exists email_filter_and_organizer;

create table if not exists core_label_metadata(
id bigint primary key auto_increment,
name varchar(255) NOT NULL,
creation_time datetime NOT NULL
);

create table if not exists core_user_profile(
id bigint primary key auto_increment,
user_email_id varchar(255) NOT NULL,
label_id bigint references core_label_metadata on delete cascade on update cascade
);

create table if not exists label_filter(
id bigint primary key auto_increment,
filter_name varchar(255),
label_id bigint references core_label_metadata on delete cascade on update cascade,
is_email_id_filter boolean,
email_id_keywords longblob,
is_subject_filter boolean,
subject_keywords longblob,
is_body_filter boolean,
body_keywords longblob,
creation_date datetime
);

create table if not exists delete_filter(
id bigint primary key auto_increment,
filter_name varchar(255),
label_id bigint references core_label_metadata on delete cascade on update cascade,
is_email_id_filter boolean,
email_id_keywords longblob,
is_subject_filter boolean,
subject_keywords longblob,
is_body_filter boolean,
body_keywords longblob,
creation_date datetime
);

create table if not exists core_run_manager(
id bigint primary key auto_increment,
label_filter_id bigint references label_filter on delete cascade on update cascade,
delete_filter_id bigint references delete_filter on delete cascade on update cascade,
run_name varchar(255),
status int,
failure_reason varchar(1024)
);