# --- !Ups
CREATE OR REPLACE FUNCTION set_timestamps() RETURNS TRIGGER AS $code$
DECLARE
  curtime TIMESTAMP := clock_timestamp();;
BEGIN
  IF (TG_OP = 'INSERT') THEN
    NEW.created_at := curtime;;
    NEW.updated_at := curtime;;
  ELSIF (TG_OP = 'UPDATE') THEN
    NEW.updated_at := curtime;;
  END IF;;
  RETURN NEW;;
END;;
$code$ LANGUAGE 'plpgsql';

# --- !Downs
DROP FUNCTION set_timestamps();
