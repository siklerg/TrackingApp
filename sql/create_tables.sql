drop table if exists users;
create table if not exists users (
	user_id serial primary key,
	username varchar(25) not null unique,
	email varchar(100) not null, 
	full_name varchar (100),
	nickname varchar (25) not null,
	last_login timestamp with time zone,
	password varchar (200) not null);

drop table if exists roles;
create table if not exists roles (
	role_id serial primary key,
	role_name varchar(20) not null);
	
drop table if exists user_role;
create table if not exists user_role (
	user_role_id serial primary key,
	user_id int references users (user_id),
	role_id int references roles (role_id));
	
drop table if exists kml_files;
create table if not exists kml_files (
	kml_file_id serial primary key,
	filename varchar (50) not null,
	file bytea not null);
	
drop table if exists tracking;
create table if not exists tracking (
	tracking_id serial primary key,
	kml_file_id int references kml_files (kml_file_id),
	user_id int references users (user_id));
	
drop table if exists pic_files;
create table if not exists pic_files (
	pic_file_id serial primary key,
	filename varchar (50) not null,
	file bytea not null,
	tracking_id int references tracking (tracking_id));
	
drop table if exists tracking_parameters;
create table if not exists tracking_parameters (
	tracking_parameter_id serial primary key,
	tracking_id int references tracking (tracking_id),
	length float not null,
	level int check (level>0 and level<6),
	description varchar (4000));
	
drop table if exists regions;
create table if not exists regions (
	region_id serial primary key,
	region_name varchar(200) not null);
	
drop table if exists tracking_regions;
create table if not exists tracking_regions (
	tracking_region_id serial primary key,
	tracking_id int references tracking (tracking_id),
	region_id int references regions (region_id));
	
insert into roles (role_name) values ('admin');    
insert into roles (role_name) values ('writer');    
insert into regions (region_name) values ('Dunamenti-síkság');
insert into regions (region_name) values ('Duna-Tisza közi síkvidék');
insert into regions (region_name) values ('Bácskai-síkvidék');
insert into regions (region_name) values ('Mezőföld');
insert into regions (region_name) values ('Drávamenti-síkság');
insert into regions (region_name) values ('Felső-Tisza vidék');
insert into regions (region_name) values ('Közép-Tisza-vidék');
insert into regions (region_name) values ('Alsó-Tisza-vidék');
insert into regions (region_name) values ('Észak-Alföldi-hordalékkúpsíkság');
insert into regions (region_name) values ('Nyírség');
insert into regions (region_name) values ('Hajdúság');
insert into regions (region_name) values ('Berettyó-Körös-vidék');
insert into regions (region_name) values ('Körös-Maros köze');
insert into regions (region_name) values ('Győri-medence');
insert into regions (region_name) values ('Marcal-medence');
insert into regions (region_name) values ('Komárom-Esztergomi-síkság');
insert into regions (region_name) values ('Alpokalja');
insert into regions (region_name) values ('Sopron-Vasi-síkság');
insert into regions (region_name) values ('Kemeneshát');
insert into regions (region_name) values ('Zalai-domvidék');
insert into regions (region_name) values ('Balaton-medence');
insert into regions (region_name) values ('Külső-Somogy');
insert into regions (region_name) values ('Belső-Somogy');
insert into regions (region_name) values ('Mecsek és Tolna-Baranyai-domvidék');
insert into regions (region_name) values ('Bakony-vidék');
insert into regions (region_name) values ('Vértes-Velencei-hegyvidék');
insert into regions (region_name) values ('Dunazug-hegyvidék');
insert into regions (region_name) values ('Visegrádi-hegység');
insert into regions (region_name) values ('Börzsöny');
insert into regions (region_name) values ('Cserhát-vidék');
insert into regions (region_name) values ('Mátra-vidék');
insert into regions (region_name) values ('Bükk-vidék');
insert into regions (region_name) values ('Aggtelek-Rudabányai-hegyvidék');
insert into regions (region_name) values ('Tokaj-Zempléni-hegyvidék');
insert into regions (region_name) values ('Észak-magyarországi medencék');