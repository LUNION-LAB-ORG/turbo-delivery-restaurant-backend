CREATE TABLE notificationswebhooks (
    id SERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    description TEXT,
    status INTEGER,
    deleted BOOLEAN DEFAULT FALSE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_edition TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    restaurant_id BIGINT,
    CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);
