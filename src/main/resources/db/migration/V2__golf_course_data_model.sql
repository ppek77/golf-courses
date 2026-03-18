-- Golf course data model

CREATE TABLE countries (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE golf_courses (
    id              BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    country_id      BIGINT       NOT NULL REFERENCES countries(id),
    description     TEXT,
    official_rating DOUBLE PRECISION,
    personal_rating DOUBLE PRECISION,
    logo_ball       BOOLEAN      NOT NULL DEFAULT FALSE,
    length_unit     VARCHAR(10)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE tees (
    id            BIGSERIAL      PRIMARY KEY,
    name          VARCHAR(50)    NOT NULL,
    course_rating DOUBLE PRECISION,
    slope_rating  DOUBLE PRECISION,
    course_id     BIGINT         NOT NULL REFERENCES golf_courses(id) ON DELETE CASCADE
);

CREATE TABLE holes (
    id        BIGSERIAL PRIMARY KEY,
    number    INT       NOT NULL,
    par       INT       NOT NULL,
    hcp       INT       NOT NULL,
    course_id BIGINT    NOT NULL REFERENCES golf_courses(id) ON DELETE CASCADE,
    UNIQUE (course_id, number)
);

CREATE TABLE hole_tee_lengths (
    id      BIGSERIAL PRIMARY KEY,
    hole_id BIGINT    NOT NULL REFERENCES holes(id) ON DELETE CASCADE,
    tee_id  BIGINT    NOT NULL REFERENCES tees(id) ON DELETE CASCADE,
    length  INT       NOT NULL,
    UNIQUE (hole_id, tee_id)
);

CREATE TABLE played_rounds (
    id        BIGSERIAL PRIMARY KEY,
    date      DATE      NOT NULL,
    course_id BIGINT    NOT NULL REFERENCES golf_courses(id),
    tee_id    BIGINT    NOT NULL REFERENCES tees(id),
    user_id   BIGINT    NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE round_scores (
    id       BIGSERIAL PRIMARY KEY,
    round_id BIGINT    NOT NULL REFERENCES played_rounds(id) ON DELETE CASCADE,
    hole_id  BIGINT    NOT NULL REFERENCES holes(id),
    score    INT       NOT NULL,
    UNIQUE (round_id, hole_id)
);
