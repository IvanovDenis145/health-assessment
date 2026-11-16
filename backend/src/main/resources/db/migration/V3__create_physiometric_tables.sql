CREATE TABLE vital_lung_capacity (
                                     id          VARCHAR(255) PRIMARY KEY,
                                     value       INTEGER      NOT NULL,
                                     user_id     VARCHAR(255) NOT NULL,
                                     created_at  TIMESTAMP,
                                     updated_at  TIMESTAMP,
                                     CONSTRAINT fk_vlc_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE dynamometry (
                             id          VARCHAR(255) PRIMARY KEY,
                             value       INTEGER      NOT NULL,
                             user_id     VARCHAR(255) NOT NULL,
                             created_at  TIMESTAMP,
                             updated_at  TIMESTAMP,
                             CONSTRAINT fk_dynamometry_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE deadlift_strength (
                                   id          VARCHAR(255) PRIMARY KEY,
                                   value       INTEGER      NOT NULL,
                                   user_id     VARCHAR(255) NOT NULL,
                                   created_at  TIMESTAMP,
                                   updated_at  TIMESTAMP,
                                   CONSTRAINT fk_deadlift_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);