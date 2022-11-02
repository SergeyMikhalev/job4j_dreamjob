CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   surname TEXT,
   description TEXT,
   registered timestamp without time zone NOT NULL,
   city_id integer,
   photo bytea
);