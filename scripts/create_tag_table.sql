CREATE TABLE tags(tag_id SERIAL,
                  tag_name TEXT NOT NULL,
                  description TEXT,
                  wiki_name TEXT,
                  wiki_url TEXT,
                  wiki_id TEXT,
                  article_id TEXT,
                  PRIMARY KEY (tag_id))


