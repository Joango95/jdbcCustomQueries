CREATE TABLE IF NOT EXISTS client (
  id TEXT PRIMARY KEY NOT NULL,
  user_name TEXT,
  email TEXT,
  phone_number INT,
  updated_time timestamp default current_timestamp,
  friend_id_list TEXT[]
);