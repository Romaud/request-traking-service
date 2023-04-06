insert into users values (1, 'admin', 'admin');
insert into users values (2, 'operator', 'operator');
insert into users values (3, 'user', 'user');
insert into users values (4, 'user', 'new user');

insert into roles values (1, 'ROLE_ADMIN');
insert into roles values (2, 'ROLE_OPERATOR');
insert into roles values (3, 'ROLE_USER');

insert into users_roles values (1, 1);
insert into users_roles values (2, 2);
insert into users_roles values (3, 3);
insert into users_roles values (4, 3);

insert into requests (id, assignee_id, created_at, status, text, updated_at, user_id)
values (1, null, current_timestamp, 'DRAFT', 'random text', current_timestamp, 3);
insert into requests (id, assignee_id, created_at, status, text, updated_at, user_id)
values (2, null, current_timestamp, 'SENT', 'random text', current_timestamp, 3);
insert into requests (id, assignee_id, created_at, status, text, updated_at, user_id)
values (3, null, current_timestamp, 'SENT', 'random text', current_timestamp, 4);
insert into requests (id, assignee_id, created_at, status, text, updated_at, user_id)
values (4, null, current_timestamp, 'DRAFT', 'random text', current_timestamp, 3);
insert into requests (id, assignee_id, created_at, status, text, updated_at, user_id)
values (5, null, current_timestamp, 'SENT', 'random text', current_timestamp, 3);