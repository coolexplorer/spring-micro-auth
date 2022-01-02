INSERT INTO role (role_name, description, modified_date, created_date)
VALUES ('ROLE_ADMIN', 'Administrator', CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP());
INSERT INTO role (role_name, description, modified_date, created_date)
VALUES ('ROLE_USER', 'User', CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP());

INSERT INTO account (email, password, first_name, last_name, created_date, modified_date)
VALUES ('admin@coolexplorer.io', '$2a$10$6x8Rms2/ES7VpSnXYD6BZeK2mpyH64ly/9QxY3rorjC.6HEMdiSBe', 'John', 'Kim', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account_roles (account_id, role_id)
VALUES (1, 1);

INSERT INTO account (email, password, first_name, last_name, created_date, modified_date)
VALUES ('test@coolexplorer.io', '$2a$10$6x8Rms2/ES7VpSnXYD6BZeK2mpyH64ly/9QxY3rorjC.6HEMdiSBe', 'test', 'Kim', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account_roles (account_id, role_id)
VALUES (2, 2);
