CREATE TABLE IF NOT EXISTS client (
  id TEXT PRIMARY KEY NOT NULL,
  user_name TEXT,
  phone_number INT,
  updated_time timestamp default current_timestamp,
  friend_id_list TEXT[]
);

INSERT INTO client (id, user_name, email, phone_number, friend_id_list)
VALUES ('1', 'Sam', '32123231', '{"2","3","4","5"}'),
    ('2', 'Samantha', '1231312', '{"1","5"}'),
    ('3', 'Joe', '12344235', '{"7","8","9"}'),
    ('4', 'Joel', '234512343', '{"6","8"}'),
    ('5', 'Joela', '45623452', '{"1","6","8","9"}'),
    ('6', 'George', '674364565', '{"1","3"}'),
    ('7', 'Samuel', '13321323', '{"4","5"}'),
    ('8', 'Nick', '34753445', '{"1","2"}'),
    ('9', 'Nicky', '734514534', '{"8"}')
;

INSERT INTO client (id, user_name, email, phone_number, friend_id_list, updated_time)
VALUES ('10', 'Juan', '24231234', '{"6","7"}', TO_TIMESTAMP(1814049600) AT TIME ZONE 'UTC'),
       ('11', 'Jose', '25362423', '{"1","10"}', TO_TIMESTAMP(1824930600) AT TIME ZONE 'UTC');
