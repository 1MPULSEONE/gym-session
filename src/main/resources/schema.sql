CREATE TABLE IF NOT EXISTS sport_type
(
    id         INT          NOT NULL UNIQUE,
    title      VARCHAR(250) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS training_diary
(
    id            INT          NOT NULL UNIQUE,
    user_id       INT          NOT NULL,
    name          VARCHAR(250) NOT NULL,
    sport_type_id INT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (sport_type_id) REFERENCES sport_type (id)
);

CREATE TABLE IF NOT EXISTS role
(
    id         INT          NOT NULL UNIQUE,
    name       VARCHAR(256) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_data
(
    id         INT          NOT NULL UNIQUE,
    password   VARCHAR(256) NOT NULL,
    username      VARCHAR(256) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_roles
(
    role_id INT NOT NULL,
    user_id    INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role (id),
    FOREIGN KEY (user_id) REFERENCES user_data (id),
    PRIMARY KEY (role_id, user_id)
);