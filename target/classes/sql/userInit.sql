CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       role VARCHAR(50)
);

INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$encodedPassword', 'ADMIN');