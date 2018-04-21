CREATE TABLE followings (
  user_id     BIGINT   NOT NULL,
  target_id   BIGINT   NOT NULL,
  target_type INTEGER  NOT NULL,
  gmt_action  DATETIME NOT NULL,
  status      INTEGER DEFAULT 1,
  CONSTRAINT uk_followings UNIQUE (user_id, target_id, target_type)
);
