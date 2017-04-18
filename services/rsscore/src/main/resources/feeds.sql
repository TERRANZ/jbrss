CREATE TABLE feeds (
  id          INT(11)                             NOT NULL AUTO_INCREMENT,
  feedname    LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  feedurl     LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  update_time DATETIME                            NOT NULL,
  PRIMARY KEY (id)
);