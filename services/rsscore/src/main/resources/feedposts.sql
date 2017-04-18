CREATE TABLE feedposts (
  id          INT(11)                             NOT NULL AUTO_INCREMENT,
  feed_id     INT(11)                             NOT NULL,
  isread      BIT(1)                              NOT NULL,
  postdate    DATETIME                            NOT NULL,
  postlink    LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  posttext    LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  posttitle   LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  update_time DATETIME                            NOT NULL,
  PRIMARY KEY (id),
  KEY fp_id_idx (id),
  KEY fp_postdate_idx (postdate)
);
