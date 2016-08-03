# --- !Ups
CREATE TABLE users (
  id         BIGSERIAL NOT NULL PRIMARY KEY,
  firstname  TEXT      NOT NULL  DEFAULT '',
  lastname   TEXT      NOT NULL  DEFAULT '',
  username   TEXT      NOT NULL  DEFAULT '',
  password   TEXT      NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TRIGGER trigger_users_set_timestamps
BEFORE INSERT OR UPDATE ON users
FOR EACH ROW
EXECUTE PROCEDURE set_timestamps();

# --- !Downs
DROP TRIGGER IF EXISTS trigger_users_set_timestamps ON users;
DROP TABLE IF EXISTS users;
