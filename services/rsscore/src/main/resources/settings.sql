CREATE TABLE settings (
  id     INT(11)                             NOT NULL AUTO_INCREMENT,
  skey   VARCHAR(128)
         COLLATE utf8mb4_unicode_ci          NOT NULL,
  svalue LONGTEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id)
);