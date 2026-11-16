CREATE TABLE glucose_level (
                               id          VARCHAR(255) PRIMARY KEY,
                               value       DOUBLE PRECISION NOT NULL,
                               user_id     VARCHAR(255)     NOT NULL,
                               created_at  TIMESTAMP,
                               updated_at  TIMESTAMP,
                               CONSTRAINT fk_glucose_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE cholesterol_level (
                                   id          VARCHAR(255) PRIMARY KEY,
                                   value       DOUBLE PRECISION NOT NULL,
                                   user_id     VARCHAR(255)     NOT NULL,
                                   created_at  TIMESTAMP,
                                   updated_at  TIMESTAMP,
                                   CONSTRAINT fk_cholesterol_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE triglyceride_level (
                                    id          VARCHAR(255) PRIMARY KEY,
                                    value       DOUBLE PRECISION NOT NULL,
                                    user_id     VARCHAR(255)     NOT NULL,
                                    created_at  TIMESTAMP,
                                    updated_at  TIMESTAMP,
                                    CONSTRAINT fk_triglyceride_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);