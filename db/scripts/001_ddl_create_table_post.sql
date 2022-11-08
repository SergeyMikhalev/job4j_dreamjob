CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created timestamp without time zone NOT NULL,
   visible boolean NOT NULL,
   city_id integer
);