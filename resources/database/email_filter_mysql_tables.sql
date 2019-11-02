#create database if not exists email_filter_and_organizer;

create table if not exists core_label_metadata(
id bigint primary key auto_increment,
name varchar(255) NOT NULL,
creation_time datetime NOT NULL
);

create table if not exists nested_label_metadata(
id bigint primary key auto_increment,
child_label_id bigint references core_label_metadata on delete cascade on update cascade,
parent_label_id bigint references core_label_metadata on delete cascade on update cascade
);

create table if not exists label_filter(
id bigint primary key,
nested_label_id	bigint references nested_label_metadata on delete cascade on update cascade,
is_email_id_filter boolean,
email_id_keywords longblob,
is_subject_filter boolean,
subject_keywords longblob,
is_body_filter boolean,
body_keywords longblob,
creation_date datetime
);

create table if not exists delete_filter(
id bigint primary key,
nested_label_id	bigint references nested_label_metadata on delete cascade on update cascade,
is_email_id_filter boolean,
email_id_keywords longblob,
is_subject_filter boolean,
subject_keywords longblob,
is_body_filter boolean,
body_keywords longblob,
creation_date datetime
);