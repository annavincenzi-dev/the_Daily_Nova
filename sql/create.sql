CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARy KEY, 
    username VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR (100) NOT NULL, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR (100) NOT NULL
);

CREATE TABLE categories(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE articles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR (100),
    subtitle VARCHAR (100),
    body TEXT, 
    published_on DATE, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT, 
    category_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE images(
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    path VARCHAR(255) NOT NULL, 
    article_id BIGINT, 
    FOREIGN KEY(article_id) REFERENCES articles(id)
);

