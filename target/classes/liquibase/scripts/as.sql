-- liquibase formatted sql
-- changeset andrey:2
create table users
(
    id           serial primary key,
    fist_name    text,
    last_name    text,
    age          int,
    number_phone int,
    email        text,
    address      text
)


-- changeset akostin:12
create table dog_shelter
(
    id           serial primary key,
    nameShelter  text,
    address      text,
    workingHours time
);

create table shelter_for_cats
(
    id           serial primary key,
    nameShelter  text,
    address      text,
    workingHours time
);

create table cats
(
    id              serial primary key,
    name_cat        TEXT,
    age             smallint,
    breed           text,
    shelter_cats_id serial,
    foreign key (shelter_cats_id) references shelter_for_cats (id),
    owner           serial,
    foreign key (owner) references users (id)
);

create table dogs
(
    id             serial primary key,
    name_dog       TEXT,
    age            smallint,
    breed          text,
    shelter_dog_id serial,
    foreign key (shelter_dog_id) references dog_shelter (id),
    owner          serial,
    foreign key (owner) references users (id)
);

create table volunteer
(
    id         serial primary key,
    first_name TEXT,
    lastName   text,
    email      text
);
--changeset akostin:13
alter table users
    rename column fist_name to first_name;

-- changeset andrey:14
alter table users
    add column telegram_user_id bigint,
add column first_login_date timestamp;

-- changeset akostin:22
alter table users
drop column telegram_user_id;

-- changeset akostin:23
alter table users
    add column telegram_user_id bigint;

