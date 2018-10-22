DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
  id               INTEGER PRIMARY KEY AUTO_INCREMENT,
  name             VARCHAR(128)            NOT NULL,
  email            VARCHAR(128)            NOT NULL,
  password         VARCHAR(128)            NOT NULL,
  registered       TIMESTAMP DEFAULT now() NOT NULL,
  enabled          BOOL DEFAULT TRUE       NOT NULL,
  calories_per_day INTEGER DEFAULT 2000    NOT NULL
);
ALTER TABLE users AUTO_INCREMENT=100000;
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR(64),
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE meals
(
  id              INTEGER PRIMARY KEY AUTO_INCREMENT,
  description     VARCHAR(128)              NOT NULL,
  date_time       TIMESTAMP DEFAULT now()   NOT NULL,
  user_id          INTEGER                   NOT NULL,
  calories        INTEGER                   NOT NULL
);

CREATE UNIQUE INDEX uniq_meal_user_date_time ON meals (user_id, date_time);

ALTER TABLE meals AUTO_INCREMENT=200000;
ALTER TABLE meals ADD CONSTRAINT fk_meals  FOREIGN KEY (user_id) REFERENCES users (id)
  ON DELETE CASCADE ON UPDATE NO ACTION;


