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



