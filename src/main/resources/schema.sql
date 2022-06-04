ALTER TABLE if EXISTS friends DROP CONSTRAINT if EXISTS fk_friends_status;
ALTER TABLE if EXISTS friends DROP CONSTRAINT if EXISTS fk_friends_friend;
ALTER TABLE if EXISTS friends DROP CONSTRAINT if EXISTS fk_friends_user;

ALTER TABLE if EXISTS likes DROP CONSTRAINT if EXISTS fk_likes_user;
ALTER TABLE if EXISTS likes DROP CONSTRAINT if EXISTS fk_likes_film;

ALTER TABLE if EXISTS films DROP CONSTRAINT if EXISTS fk_film_rating;

ALTER TABLE if EXISTS film_genre DROP CONSTRAINT if EXISTS fk_film_genre_film;
ALTER TABLE if EXISTS film_genre DROP CONSTRAINT if EXISTS fk_film_genre_genre;


DROP TABLE if EXISTS friends CASCADE ;
DROP TABLE if EXISTS likes CASCADE;
DROP TABLE if EXISTS films CASCADE;
DROP TABLE if EXISTS film_genre CASCADE;
DROP TABLE if EXISTS users CASCADE;
DROP TABLE if EXISTS genre CASCADE;
DROP TABLE if EXISTS friend_status CASCADE;
DROP TABLE if EXISTS mpa CASCADE;



CREATE TABLE users(
  user_id bigint not null generated by default as identity,
  name varchar (255) not null,
  email varchar (255) not null,
  login varchar (255),
  birthday date,
  PRIMARY KEY (user_id)
);

CREATE TABLE friend_status (
    status_id bigint not null generated by default as identity,
    name varchar (255) not null,
    PRIMARY KEY (status_id)
);

CREATE TABLE friends(
    user_id bigint not null,
    friend_id bigint not null,
    status_id bigint not null
);

CREATE TABLE films (
    film_id bigint not null generated by default as identity,
    name varchar (255) not null,
    description varchar (255),
    release date not null,
    duration bigint not null,
    rating_id bigint not null,
    PRIMARY KEY (film_id)
);

CREATE TABLE likes (
    user_id bigint not null,
    film_id bigint not null
);

CREATE TABLE mpa (
    mpa_id bigint not null generated by default as identity,
    name varchar (255) not null ,
    description varchar (255) not null ,
    PRIMARY KEY (mpa_id)
);

CREATE TABLE genre (
    genre_id bigint not null generated by default as identity,
    name varchar (255) not null,
    PRIMARY KEY (genre_id)
);

CREATE TABLE film_genre (
    film_id bigint not null,
    genre_id bigint not null
);

ALTER TABLE if EXISTS friends ADD CONSTRAINT fk_friends_status FOREIGN KEY (status_id) REFERENCES friend_status (status_id);
ALTER TABLE if EXISTS friends ADD CONSTRAINT fk_friends_friend FOREIGN KEY (friend_id) REFERENCES users (user_id);
ALTER TABLE if EXISTS friends ADD CONSTRAINT fk_friends_user FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE if EXISTS likes ADD CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE if EXISTS likes ADD CONSTRAINT fk_likes_film FOREIGN KEY (film_id) REFERENCES films (film_id);

ALTER TABLE if EXISTS films ADD CONSTRAINT fk_film_rating FOREIGN KEY (rating_id) REFERENCES mpa (mpa_id);

ALTER TABLE if EXISTS film_genre ADD CONSTRAINT fk_film_genre_film FOREIGN KEY (film_id) REFERENCES films (film_id);
ALTER TABLE if EXISTS film_genre ADD CONSTRAINT fk_film_genre_genre FOREIGN KEY (genre_id) REFERENCES genre (genre_id);
