CREATE TABLE IF NOT EXISTS users(user_id SERIAL,
                  user_name TEXT NOT NULL,
                  email_address TEXT,
                  pass TEXT,
                  PRIMARY KEY (user_id))