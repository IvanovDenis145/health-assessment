CREATE TABLE heart_rate (
                            id          VARCHAR(255) PRIMARY KEY,
                            value       INTEGER      NOT NULL,
                            user_id     VARCHAR(255) NOT NULL,
                            created_at  TIMESTAMP,
                            updated_at  TIMESTAMP,
                            CONSTRAINT fk_heartrate_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE blood_pressure (
                                id              VARCHAR(255) PRIMARY KEY,
                                upper_pressure  INTEGER      NOT NULL,
                                lower_pressure  INTEGER      NOT NULL,
                                user_id         VARCHAR(255) NOT NULL,
                                created_at      TIMESTAMP,
                                updated_at      TIMESTAMP,
                                CONSTRAINT fk_bp_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE respiratory_rate (
                                  id          VARCHAR(255) PRIMARY KEY,
                                  value       INTEGER      NOT NULL,
                                  user_id     VARCHAR(255) NOT NULL,
                                  created_at  TIMESTAMP,
                                  updated_at  TIMESTAMP,
                                  CONSTRAINT fk_resp_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);