CREATE TABLE `users` (
    id              BIGINT(20)  AUTO_INCREMENT,
    name            VARCHAR(15) NOT NULL,
    email           VARCHAR(63) NOT NULL,
    created_at      DATETIME(6) NOT NULL,
    updated_at      DATETIME(6) NOT NULL,
    PRIMARY KEY PK__users__id (id),
    CONSTRAINT UK__users__email UNIQUE (email)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX IDX__users__created_at ON users(created_at) ALGORITHM=INPLACE LOCK=NONE;
CREATE INDEX IDX__users__updated_at ON users(updated_at) ALGORITHM=INPLACE LOCK=NONE;
