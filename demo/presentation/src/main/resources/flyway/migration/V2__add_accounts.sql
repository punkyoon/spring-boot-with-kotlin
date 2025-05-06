CREATE TABLE `accounts` (
    user_id         BIGINT(20)  AUTO_INCREMENT,
    username        VARCHAR(63) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    created_at      DATETIME(6) NOT NULL,
    updated_at      DATETIME(6) NOT NULL,
    PRIMARY KEY PK__accounts__user_id (user_id),
    CONSTRAINT UK__accounts__username UNIQUE (username)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX IDX__accounts__created_at ON accounts(created_at) ALGORITHM=INPLACE LOCK=NONE;
CREATE INDEX IDX__accounts__updated_at ON accounts(updated_at) ALGORITHM=INPLACE LOCK=NONE;
