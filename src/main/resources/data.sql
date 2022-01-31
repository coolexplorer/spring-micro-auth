INSERT INTO role (role_name, description, modified_date, created_date)
VALUES ('ROLE_ADMIN', 'Administrator', CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP());
INSERT INTO role (role_name, description, modified_date, created_date)
VALUES ('ROLE_USER', 'User', CURRENT_TIMESTAMP (), CURRENT_TIMESTAMP());

INSERT INTO account (email, password, first_name, last_name, created_date, modified_date)
VALUES ('admin@coolexplorer.io', '$2a$10$2/GCd0eV1rjS3SUaSqTmZe2KnX40vNPJQtU3mawtoGWi9NBbjX1oO', 'John', 'Kim', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account_roles (account_id, role_id)
VALUES (1, 1);

INSERT INTO account (email, password, first_name, last_name, created_date, modified_date)
VALUES ('test@coolexplorer.io', '$2a$10$2/GCd0eV1rjS3SUaSqTmZe2KnX40vNPJQtU3mawtoGWi9NBbjX1oO', 'test', 'Kim', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account_roles (account_id, role_id)
VALUES (2, 2);
