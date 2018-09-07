CREATE DATABASE pilgerdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd';
GRANT ALL ON pilgerdb.* TO 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd' WITH GRANT OPTION;


create table Pmail (
id  serial not null,
comment varchar(255),
uuid varchar(255),
optlock integer DEFAULT 0 not null,
pcontent text,
pfrom varchar(255),
psubject varchar(255),
primary key (id)
)
