INSERT INTO accounts_user(
    id, username, first_name, middle_name, last_name, email, birth_date, disabled
) VALUES ('ebb8c083-de7d-4681-94e6-b1675aca0061', 'user', 'Пользователь', 'Пользователь', 'Пользователь', 'user1@email.com', '1993-03-11', false);

INSERT INTO accounts_user(
    id, username, first_name, middle_name, last_name, email, birth_date, disabled
) VALUES ('02f5dd65-3fb9-48ed-b2fe-bed75273f728', 'user2', 'Пользователь2', 'Пользователь2', 'Пользователь2', 'user2@email.com', '1993-03-01', false);

INSERT INTO account_role(
    id, role_name
) VALUES ('add36209-ed0c-41d2-96bf-96451bdeb9f8', 'CREATE');


INSERT INTO account_role(
    id, role_name
) VALUES ('a8b71cfd-a02e-45db-a4aa-d01ea4fa50f8', 'VIEW');


INSERT INTO account_role(
    id, role_name
) VALUES ('c0c6d7c7-448e-479e-8440-c668975a7a68', 'ADMIN');

INSERT INTO account_to_role(
    account_id, role_id
) VALUES ('ebb8c083-de7d-4681-94e6-b1675aca0061', 'add36209-ed0c-41d2-96bf-96451bdeb9f8');

INSERT INTO account_to_role(
    account_id, role_id
) VALUES ('ebb8c083-de7d-4681-94e6-b1675aca0061', 'a8b71cfd-a02e-45db-a4aa-d01ea4fa50f8');

INSERT INTO account_to_role(
    account_id, role_id
) VALUES ('ebb8c083-de7d-4681-94e6-b1675aca0061', 'c0c6d7c7-448e-479e-8440-c668975a7a68');

INSERT INTO account_to_role(
    account_id, role_id
) VALUES ('02f5dd65-3fb9-48ed-b2fe-bed75273f728', 'a8b71cfd-a02e-45db-a4aa-d01ea4fa50f8');