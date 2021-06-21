CREATE TABLE IF NOT EXISTS articles(
    article_id character varying(255) NOT NULL,
    title text NOT NULL,
    journal text,
    publish_date date NOT NULL,
    revision_date date NOT NULL,
    lang character varying(255),
    abstract text NOT NULL,
    authors text[],
    keywords text[],
    tags text[],
    PRIMARY KEY (article_id)
);

CREATE TABLE IF NOT EXISTS articles_lookup(article_id varchar(255) NOT NULL, PRIMARY KEY (article_id));

CREATE TABLE IF NOT EXISTS tags(tag_id SERIAL,
                  tag_name TEXT NOT NULL,
                  description TEXT,
                  wiki_name TEXT,
                  wiki_url TEXT,
                  wiki_id TEXT,
                  article_id TEXT,
                  PRIMARY KEY (tag_id));

CREATE TABLE IF NOT EXISTS users(user_id SERIAL,
                                 user_name TEXT NOT NULL,
                                 email_address TEXT,
                                 pass TEXT,
                                 PRIMARY KEY (user_id));

CREATE EXTENSION pgcrypto;