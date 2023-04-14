--liquibase formatted sql

-- changeset aleksandr:1
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'image'
CREATE TABLE image
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  VARCHAR(255),
    file_size  BIGINT,
    media_type VARCHAR(255),
    data       BYTEA
);

-- changeset aleksandr:2
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'avatar'
CREATE TABLE avatar
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  VARCHAR(255),
    file_size  BIGINT,
    media_type VARCHAR(255),
    data       BYTEA
);

-- changeset aleksandr:3
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'user'
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(45),
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    password   VARCHAR(100),
    email      VARCHAR(255),
    phone      VARCHAR(255),
    reg_date   TIMESTAMP,
    enabled    BOOLEAN,
    avatar_id  BIGINT REFERENCES avatar (id)
);

-- changeset aleksandr:4
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'authorities'
CREATE TABLE authorities
(
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(45),
    authority VARCHAR(45)
);

-- changeset aleksandr:5
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM users WHERE username = 'user@gmail.com'
INSERT INTO users (username, password, enabled)
VALUES ('user@gmail.com', '{bcrypt}$2a$10$jd38QM.HRVwbHDFQkzuoQeNDb9lDBCLZFPMkEpE7s6mGbptKmfKhC', TRUE);

-- changeset aleksandr:6
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM authorities WHERE username = 'user@gmail.com'
INSERT INTO authorities (username, authority)
VALUES ('user@gmail.com', 'ROLE_ADMIN');

-- changeset aleksandr:7
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'ads'
CREATE TABLE ads
(
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255),
    price       NUMERIC(7, 2),
    title       VARCHAR(255),
    image_id    BIGINT REFERENCES image (id),
    author_id   BIGINT REFERENCES users (id)
);

-- changeset aleksandr:8
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'comment'
CREATE TABLE comment
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    text       VARCHAR(255),
    author_id  BIGINT REFERENCES users (id),
    ads_id     BIGINT REFERENCES ads (id)
);



