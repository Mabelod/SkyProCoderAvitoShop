--liquibase formatted sql

-- changeset aleksandr:1
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'image'
CREATE TABLE image
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  TEXT,
    file_size  BIGINT,
    media_type TEXT,
    data       BYTEA
);

-- changeset aleksandr:2
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'avatar'
CREATE TABLE avatar
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  TEXT,
    file_size  BIGINT,
    media_type TEXT,
    data       BYTEA
);

-- changeset aleksandr:3
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'user'
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    user_name   TEXT,
    first_name TEXT,
    last_name  TEXT,
    password   VARCHAR(16),
    email      TEXT,
    phone      TEXT,
    reg_date   TIMESTAMP,
    role       VARCHAR(5),
    avatar_id  BIGINT REFERENCES avatar (id)
);

-- changeset aleksandr:4
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'ads'
CREATE TABLE ads
(
    id          BIGSERIAL PRIMARY KEY,
    description TEXT,
    price       NUMERIC(7, 2),
    title       TEXT,
    image_id    BIGINT REFERENCES image (id),
    author_id   BIGINT REFERENCES users (id)
);

-- changeset aleksandr:5
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'comment'
CREATE TABLE comment
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    text       TEXT,
    author_id  BIGINT REFERENCES users (id)
);



