create table if not exists core_label_metadata(
id serial primary key,
name varchar(255) NOT NULL,
creation_time timestamp NOT NULL
);

create table if not exists nested_label_metadata(
id serial primary key,
child_label_id int references core_label_metadata on delete cascade on update cascade,
parent_label_id int references core_label_metadata on delete cascade on update cascade
);

create table if not exists label_filter(
id serial primary key,
nested_label_id	int references nested_label_metadata on delete cascade on update cascade,
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
nested_label_id	int references nested_label_metadata on delete cascade on update cascade,
is_email_id_filter boolean,
email_id_keywords bytea,
is_subject_filter boolean,
subject_keywords bytea,
is_body_filter boolean,
body_keywords bytea,
creation_date timestamp
);