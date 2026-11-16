CREATE TABLE stress_level (
                              id          VARCHAR(255) PRIMARY KEY,
                              value       INTEGER      NOT NULL,
                              user_id     VARCHAR(255) NOT NULL,
                              created_at  TIMESTAMP,
                              updated_at  TIMESTAMP,
                              CONSTRAINT fk_stress_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE mental_disorder (
                                 id          VARCHAR(255) PRIMARY KEY,
                                 name        VARCHAR(50)  NOT NULL,
                                 user_id     VARCHAR(255) NOT NULL,
                                 created_at  TIMESTAMP,
                                 updated_at  TIMESTAMP,
                                 CONSTRAINT fk_mental_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);