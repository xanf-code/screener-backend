CREATE TABLE users (
                                     user_id UUID PRIMARY KEY,
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     password TEXT NOT NULL,
                                     first_name VARCHAR(100) NOT NULL,
                                     last_name VARCHAR(100) NOT NULL,
                                     username VARCHAR(20) UNIQUE NOT NULL,
                                     account_verified BOOLEAN DEFAULT FALSE,
                                     verification_code INTEGER,
                                     verification_expires_at TIMESTAMP,
                                     enabled BOOLEAN DEFAULT FALSE,
                                     reset_password_token VARCHAR(255) DEFAULT NULL,
                                     reset_password_expires_at TIMESTAMP DEFAULT NULL,
                                     created_at date NOT NULL,
                                     updated_at date DEFAULT NULL
    );

CREATE TABLE user_preferred_industries (
                                           user_id UUID NOT NULL,
                                           preferred_industry VARCHAR(255),
                                           FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE watchlist (
                           id UUID PRIMARY KEY,
                           user_id UUID REFERENCES users(user_id),
                           stock_symbol VARCHAR(10) NOT NULL,
                           alert_threshold DECIMAL(5,2) NOT NULL,
                           active BOOLEAN DEFAULT TRUE,
                           added_at TIMESTAMP DEFAULT now()
);