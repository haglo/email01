CREATE DATABASE pilgerdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd';
GRANT ALL ON pilgerdb.* TO 'pilgeruser'@'localhost' IDENTIFIED BY '123atgfd' WITH GRANT OPTION;


create table pilgerdb.Pmail (
id  serial not null,
comment varchar(255),
uuid varchar(255),
optlock integer DEFAULT 0 not null,
pfrom varchar(255),
psubject varchar(255),
precipientTO text,
precipientCC text,
precipientBCC text,
pcontent text,
primary key (id)
);


select psubject, pfrom, 
convert_from(loread(lo_open(precipientCC::int, x'40000'::int), x'40000'::int),  'UTF8')
from pmail;

select psubject, pfrom,
convert_from(loread(lo_open(pmessage::int, x'40000'::int), x'40000'::int),  'UTF8')
from pmail;