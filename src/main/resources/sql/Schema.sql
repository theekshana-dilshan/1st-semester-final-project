CREATE DATABASE serisaraNetworks;

USE serisaraNetworks;

CREATE TABLE user(
                     userId VARCHAR (25) PRIMARY KEY,
                     userName VARCHAR (100),
                     password VARCHAR (100),
                     email VARCHAR (100),
                     img longblob
);

CREATE TABLE employee(
                         empId VARCHAR (25) PRIMARY KEY,
                         name VARCHAR (100),
                         address VARCHAR (100),
                         position VARCHAR (50),
                         contact VARCHAR (11),
                         salary INT(15),
                         salaryStatus VARCHAR (10),
                         userId VARCHAR (25),
                         CONSTRAINT FOREIGN KEY(userId) REFERENCES user(userId) on Delete Cascade on Update Cascade
);

CREATE TABLE customer(
                         cId VARCHAR (25) PRIMARY KEY,
                         name VARCHAR (100),
                         email VARCHAR (100),
                         address VARCHAR (100),
                         contact VARCHAR (11),
                         userId VARCHAR (25),
                         CONSTRAINT FOREIGN KEY(userId) REFERENCES user(userId) on Delete Cascade on Update Cascade
);

CREATE TABLE handoverDevice(
                               deviceId VARCHAR (25) PRIMARY KEY,
                               dName VARCHAR (100),
                               problem VARCHAR (200),
                               status VARCHAR (100),
                               cost INT (25),
                               cId VARCHAR (25),
                               CONSTRAINT FOREIGN KEY(cId) REFERENCES customer(cId) on Delete Cascade on Update Cascade
);

CREATE TABLE payment(
                        pId VARCHAR (25) PRIMARY KEY,
                        amount INT (25),
                        status VARCHAR (50),
                        date DATE
);

CREATE TABLE item(
                     itemId VARCHAR (25) PRIMARY KEY,
                     itemName VARCHAR (150),
                     itemCategory VARCHAR (50),
                     qtyOnHand INT (20),
                     cost INT (20),
                     unitPrice INT (20)
);

CREATE TABLE supplier(
                         supId VARCHAR (25) PRIMARY KEY,
                         sName VARCHAR (100),
                         address VARCHAR (150),
                         contact INT (10)
);

CREATE TABLE driver(
                       driverId VARCHAR (25) PRIMARY KEY,
                       name VARCHAR (100),
                       contact INT (10),
                       address VARCHAR (150)
);

CREATE TABLE vehicle(
                        vId VARCHAR (25) PRIMARY KEY,
                        vehicleName VARCHAR (100),
                        vehicleNum VARCHAR (20),
                        driverId VARCHAR (25),
                        CONSTRAINT FOREIGN KEY(driverId) REFERENCES driver(driverId) on Delete Cascade on Update Cascade
);

CREATE TABLE delivery(
                         deliveryId VARCHAR (25) PRIMARY KEY,
                         address VARCHAR (150),
                         contact INT (10),
                         vId VARCHAR (25),
                         CONSTRAINT FOREIGN KEY(vId) REFERENCES vehicle(vId) on Delete Cascade on Update Cascade
);

CREATE TABLE orders(
                       orderId VARCHAR (25) PRIMARY KEY,
                       qty INT (10),
                       date DATE,
                       cId VARCHAR (25),
                       pId VARCHAR (25),
                       deliveryId VARCHAR (25),
                       CONSTRAINT FOREIGN KEY(cId) REFERENCES customer(cId) on Delete Cascade on Update Cascade,
                       CONSTRAINT FOREIGN KEY(pId) REFERENCES payment(pId) on Delete Cascade on Update Cascade,
                       CONSTRAINT FOREIGN KEY(deleveryId) REFERENCES delivery(deliveryId) on Delete Cascade on Update Cascade
);

CREATE TABLE orderItemDetail(
                                itemId VARCHAR (25),
                                orderId VARCHAR (25),
                                CONSTRAINT FOREIGN KEY(itemId) REFERENCES item(itemId) on Delete Cascade on Update Cascade,
                                CONSTRAINT FOREIGN KEY(orderId) REFERENCES orders(orderId) on Delete Cascade on Update Cascade
);

CREATE TABLE itemSupplierDetail(
                                   itemId VARCHAR (25),
                                   supId VARCHAR (25),
                                   CONSTRAINT FOREIGN KEY(itemId) REFERENCES item(itemId) on Delete Cascade on Update Cascade,
                                   CONSTRAINT FOREIGN KEY(supId) REFERENCES supplier(supId) on Delete Cascade on Update Cascade
);
