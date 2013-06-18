CREATE TABLE feed (
  _id INTEGER PRIMARY KEY,
  feed_name text,
  feed_unread integer,
  feed_url text,
  feed_updated integer,
  ext_id integer
);

CREATE TABLE post (
  _id INTEGER PRIMARY KEY,
  ext_id integer,
  post_feed_id integer,
  post_date integer,
  post_title text,
  post_link text,
  post_text text,
  post_read text,
  foreign key (post_feed_id) references feed(ext_id)
);

insert into feed(feed_name,feed_unread,ext_id)  values("Все записи",0,-1);