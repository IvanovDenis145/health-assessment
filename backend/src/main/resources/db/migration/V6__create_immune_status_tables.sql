CREATE TABLE level_of_immunoglobulins (
                                          id          VARCHAR(255) PRIMARY KEY,
                                          value       DOUBLE PRECISION NOT NULL,
                                          user_id     VARCHAR(255)     NOT NULL,
                                          created_at  TIMESTAMP,
                                          updated_at  TIMESTAMP,
                                          CONSTRAINT fk_immunoglobulins_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE phagocytic_index (
                                  id          VARCHAR(255) PRIMARY KEY,
                                  value       DOUBLE PRECISION NOT NULL,
                                  user_id     VARCHAR(255)     NOT NULL,
                                  created_at  TIMESTAMP,
                                  updated_at  TIMESTAMP,
                                  CONSTRAINT fk_phagocytic_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);