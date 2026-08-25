CREATE TABLE IF NOT EXISTS tech_trend (
    id           VARCHAR(255)  NOT NULL PRIMARY KEY,
    title        VARCHAR(512)  NOT NULL,
    summary      VARCHAR(2048) NOT NULL,
    topic        VARCHAR(64)   NOT NULL,
    published_at VARCHAR(64)   NOT NULL,
    source       VARCHAR(128)  NOT NULL
);
