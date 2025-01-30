
drop table if exists friendrequest;
drop table if exists friends;
drop table if exists trading;
drop table if exists card;
drop table if exists package;
drop table if exists deck;
drop table if exists userprofile;
drop table if exists statistik;
drop table if exists users;



create table users(
	userid varchar(255) primary key not null,
	Username varchar(255) unique not null,
	password varchar(255) not null,
	Coins int not null
);


create table userprofile(
	id varchar(255) primary key not null,
	name varchar(255),
	bio varchar(255),
	image varchar(255),
	userid varchar(255) unique,
	constraint FK_User foreign key(userid) references users(userid)
);


create table statistik(
	statistikid varchar(255) primary key not null, 
	name varchar(255) unique,
	elo int,
	wins int,
	losses int,
	winloseratio decimal,
	userid varchar(255) unique,
	draw int,
	constraint FK_User foreign key(userid) references users(userid)
	
);

create table package(
	PackageID varchar(255) primary key,
	cost int
);


create table deck(
    deckid varchar(255) primary key,
    userid varchar(255) unique,
    constraint FK_User foreign key(userid) references users(userid)

);


create table card(
	cardid varchar(255) primary key not null,
	Name varchar(255) not null,
	Damage int not null,
	typ varchar(255) not null ,
	weakness varchar(255),
	typeweakness varchar(255),
	NameAndType varchar(255) not null,
	packageid varchar(255),
	userid varchar(255),
	deckid varchar(255),
	
	constraint FK_Package foreign key(packageid) references Package(PackageID),
	constraint FK_User foreign key(userid) references users(userid),
	constraint FK_deck foreign key(deckid) references deck(deckid)
	
);

create table trading(
    id varchar(255) primary key not null,
    cardtotrade varchar(255),
    type varchar(255),
    minimumdamage int,
    constraint FK_Card foreign key(cardtotrade) references card(cardid)
);


create table friends(
    id varchar(255) primary key not null,
    status varchar(255),
    username1 varchar(255),
    username2 varchar(255)
);

create table friendrequest(
    id varchar(255) primary key not null,
    userid varchar(255),
    sender varchar(255),
    constraint FK_User foreign key(userid) references users(userid)
);




select * from "users";
select * from "package";
select * from "card";
select * from "deck";
select * from "statistik";
select * from "trading";
select * from "friends";
select * from "friendrequest";
select * from userprofile u;