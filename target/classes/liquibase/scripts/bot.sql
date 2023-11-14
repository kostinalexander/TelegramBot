-- liquibase formatted sql
-- changeset andrey:1
create table users
(
    id           serial primary key,
    first_login_date timestamp,
    telegram_user_id bigint,
    first_name    text,
    last_name    text,
    age          int,
    number_phone int,
    email        text,
    address      text
)


-- changeset akostin:2
create table dog_shelter
(
    id           serial primary key,
    name_shelter  text,
    address      text,
    working_hours text
);

create table shelter_for_cats
(
    id           serial primary key,
    name_shelter  text,
    address      text,
    working_hours text
);

create table cats
(
    id              serial primary key,
    name        TEXT,
    age             smallint,
    breed           text,
    shelter_cats_id serial,
    foreign key (shelter_cats_id) references shelter_for_cats (id),
    owner           int null,
    foreign key (owner) references users (id)
);

create table dogs
(
    id             serial primary key,
    name       TEXT,
    age            smallint,
    breed          text,
    shelter_dog_id serial,
    foreign key (shelter_dog_id) references dog_shelter (id),
    owner          int null,
    foreign key (owner) references users (id)
);

create table volunteer
(
    id         serial primary key,
    first_name text,
    lastName   text,
    email      text
);

--changeset andrey:3
create table report_cat(
id serial primary key,
photo text,
local_date date,
report_text text,
cat_id serial,
foreign  key (cat_id) references cats(id)
);

create table report_dog(
id serial primary key,
photo text,
local_date date,
report_text text,
dog_id serial,
foreign  key (dog_id) references dogs(id)
);
--changeset andrey:4
alter table report_cat
 add column report_checked boolean;
 alter table report_dog
 add column report_checked boolean;


