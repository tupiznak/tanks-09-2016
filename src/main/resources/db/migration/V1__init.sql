create table users (
  id serial primary key,
  login varchar(45) DEFAULT NULL UNIQUE,
  email VARCHAR(45) DEFAULT NULL,
  password VARCHAR(45) DEFAULT NULL
);
