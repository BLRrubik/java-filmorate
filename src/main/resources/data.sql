insert into users (name, login, email, birthday) values ('Roma', 'Roma', 'g@gmail.com', '2002-12-12');
insert into users (name, login, email, birthday) values ('Dasha', 'Dasha', 'g@gmail.com', '2002-11-11');
insert into friend_status (name) values ('accepted');
insert into friends (user_id, friend_id, status_id) values (1, 2, 1);
insert into friends (user_id, friend_id, status_id) values (2, 1, 1);