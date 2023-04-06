insert into users values (1, 'admin', 'admin');
insert into users values (2, 'operator', 'operator');
insert into users values (3, 'user', 'user');
insert into users values (4, 'newUser', 'user');
insert into users values (5, 'user', 'newUser');

insert into roles values (1, 'ROLE_ADMIN');
insert into roles values (2, 'ROLE_OPERATOR');
insert into roles values (3, 'ROLE_USER');

insert into users_roles values (1, 1);
insert into users_roles values (2, 2);
insert into users_roles values (3, 3);
insert into users_roles values (3, 1);
insert into users_roles values (4, 3);
insert into users_roles values (5, 3);