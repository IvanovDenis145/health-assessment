CREATE TABLE subjective_well_being (
                                       id          VARCHAR(255) PRIMARY KEY,
                                       value       INTEGER      NOT NULL,
                                       user_id     VARCHAR(255) NOT NULL,
                                       created_at  TIMESTAMP,
                                       updated_at  TIMESTAMP,
                                       CONSTRAINT fk_wellbeing_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE state_of_health (
                                 id          VARCHAR(255) PRIMARY KEY,
                                 value       DOUBLE PRECISION NOT NULL,
                                 name        VARCHAR(20),
                                 user_id     VARCHAR(255)     NOT NULL,
                                 created_at  TIMESTAMP,
                                 updated_at  TIMESTAMP,
                                 CONSTRAINT fk_state_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);