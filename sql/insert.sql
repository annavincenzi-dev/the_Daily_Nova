INSERT INTO users (username, email, password, created_at) VALUES ('admin', 'admin@dailynova.com', '$2a$10$oMiUOq5ToRfUI/Zprg5nE.qt8nT9KKJZoDBu1SIWuj.UGx8aRHwxS', '20250624');

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN'), ('ROLE_REVISOR'), ('ROLE_WRITER');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 2);

INSERT INTO categories (name) VALUES ('News'), ('Politics'), ('Business'), ('Sports'), ('Entertainment'), ('Tech');

