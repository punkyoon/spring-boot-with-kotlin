CREATE TABLE `goals` (
    id              BIGINT(20) AUTO_INCREMENT,
    user_id         BIGINT(20) NOT NULL,

    title           VARCHAR(63) NOT NULL,
    description     VARCHAR(255) NOT NULL,

    start_date      DATE NULL DEFAULT NULL,
    end_date        DATE NULL DEFAULT NULL,

    completed       TINYINT(1) NOT NULL DEFAULT 0,

    created_at      DATETIME(6) NOT NULL,
    updated_at      DATETIME(6) NOT NULL,
    PRIMARY KEY PK__goals__id (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX IDX__goals__user_id ON goals(user_id) ALGORITHM=INPLACE LOCK=NONE;
CREATE INDEX IDX__goals__created_at ON goals(created_at) ALGORITHM=INPLACE LOCK=NONE;
CREATE INDEX IDX__goals__updated_at ON goals(updated_at) ALGORITHM=INPLACE LOCK=NONE;

CREATE TABLE `goal_progresses` (
    goal_id         BIGINT(20) NOT NULL,

    progress_rate   INT NOT NULL DEFAULT 0,

    created_at      DATETIME(6) NOT NULL,
    updated_at      DATETIME(6) NOT NULL,
    PRIMARY KEY PK__goal_progresses__goal_id (goal_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX IDX__goal_progresses__created_at ON goal_progresses(created_at) ALGORITHM=INPLACE LOCK=NONE;
CREATE INDEX IDX__goal_progresses__updated_at ON goal_progresses(updated_at) ALGORITHM=INPLACE LOCK=NONE;
