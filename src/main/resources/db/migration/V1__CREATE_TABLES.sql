CREATE TABLE hibernate_sequence (
    next_val BIGINT
) engine=MyISAM;

INSERT INTO hibernate_sequence VALUES (1);

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    password VARCHAR(64) NOT NULL,
    login VARCHAR(64) NOT NULL UNIQUE,
    nickname VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE user_groups (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE record (
    id BIGINT NOT NULL AUTO_INCREMENT,
    group_id BIGINT NOT NULL,
    record_info_id BIGINT,
    date DATE NOT NULL,
    status VARCHAR(64),
    title VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
) engine=MyISAM;

CREATE TABLE record_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_description TEXT,
    rating INT,
    link TEXT NOT NULL,
    PRIMARY KEY (id)
) engine=MyISAM;
