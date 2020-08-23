DROP DATABASE coronakitDB;

CREATE DATABASE coronakitDB;

USE coronakitDB;

CREATE TABLE Products(
	pId INT AUTO_INCREMENT PRIMARY KEY,
	pname varchar(30) not null,
	pcost varchar(10) not null,
	pdesc varchar(100) not null
);

INSERT INTO Products(pname,pcost,pdesc) VALUES
("Face mask","200.50","Face mask to protect yourself"),
("Sanitizers","100","Sanitizer to kill the germs"),
("Immunity Boosters medicines","500","Medicines to improve your immunity"),
("Gloves","300","Hand Gloves for safety"),
("Face Shield","1000" ,"Face Shield to stay away from virus"); 


CREATE TABLE coronaKit(
	coronaKitId INT AUTO_INCREMENT PRIMARY KEY,
	pname varchar(30) not null,
	pemail varchar(30) not null,
	pcontact char(10) not null,
	totalBillAmt DOUBLE(10,2) ,
	vAddress varchar(500),  
	orderDate date,
	orderFinalized ENUM ('yes','no') DEFAULT 'no'
);


CREATE TABLE kitDetails(
	kitId INT AUTO_INCREMENT PRIMARY KEY,
	coronaKitId INT,
	pId INT not null,
	pQty INT not null, 
	amt DOUBLE(10,2),
    FOREIGN KEY (coronaKitId) 
        REFERENCES coronakitDB.coronaKit(coronaKitId)
);


