CREATE TABLE articles
(
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
)