CREATE TABLE height (
                        id          VARCHAR(255) PRIMARY KEY,
                        value       INTEGER      NOT NULL,
                        user_id     VARCHAR(255) NOT NULL,
                        created_at  TIMESTAMP,
                        updated_at  TIMESTAMP,
                        CONSTRAINT fk_height_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE weight (
                        id          VARCHAR(255) PRIMARY KEY,
                        value       INTEGER      NOT NULL,
                        user_id     VARCHAR(255) NOT NULL,
                        created_at  TIMESTAMP,
                        updated_at  TIMESTAMP,
                        CONSTRAINT fk_weight_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE chest_circumference (
                                     id          VARCHAR(255) PRIMARY KEY,
                                     value       INTEGER      NOT NULL,
                                     user_id     VARCHAR(255) NOT NULL,
                                     created_at  TIMESTAMP,
                                     updated_at  TIMESTAMP,
                                     CONSTRAINT fk_chest_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE thickness_of_the_skin_fat_fold (
                                                id          VARCHAR(255) PRIMARY KEY,
                                                value       INTEGER      NOT NULL,
                                                user_id     VARCHAR(255) NOT NULL,
                                                created_at  TIMESTAMP,
                                                updated_at  TIMESTAMP,
                                                CONSTRAINT fk_skinfold_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);