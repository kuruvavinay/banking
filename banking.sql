create database bank;
use bank;
create table user(name varchar(250), email varchar(250) primary key, password varchar(250));
insert into user(name,email,password) values ('vittal','abc@gmail.com','777777'),('vinay','def@gmail.com','6666666');
create table accounts(acc_no integer primary key, name varchar(250), email varchar(250), balance float, pin integer);
