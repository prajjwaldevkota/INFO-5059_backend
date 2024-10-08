INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email)
VALUES ('123 MapleSt','London','On', 'N1N-1N1','(555)555-5555','Trusted','ABC Supply Co.','abc@supply.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) 
VALUES ('543 Sycamore Ave','Toronto','On', 'N1P-1N1','(999)555-5555','Trusted','Big Bills Depot','bb@depot.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) 
VALUES ('922 Oak St','London','On', 'N1N-1N1','(555)555-5599','Untrusted','Shady Sams','ss@underthetable.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) 
VALUES ('999 Argyl St','London','On', 'N1w-1N9','(226)226-5599','Trusted','Prajjwal Devkota','pd@vendor.com');


INSERT INTO Product (id, vendorid, name, costprice, msrp, rop, eoq, qoh, qoo, qrcode, qrcodetxt)
VALUES ('A7F9G2', 1, 'Gadget Pro 3000', 29.99, 39.99, 10, 50, 100, 5, null, null);
INSERT INTO Product (id, vendorid, name, costprice, msrp, rop, eoq, qoh, qoo, qrcode, qrcodetxt)
VALUES ('Z4X8P3', 2, 'Eco-Friendly Widget', 19.99, 29.99, 15, 30, 200, 10, null, null);
INSERT INTO Product (id, vendorid, name, costprice, msrp, rop, eoq, qoh, qoo, qrcode, qrcodetxt)
VALUES ('M2Q5K1', 3, 'SmartHome Hub', 49.99, 69.99, 5, 20, 150, 2, null, null);
INSERT INTO Product (id, vendorid, name, costprice, msrp, rop, eoq, qoh, qoo, qrcode, qrcodetxt)
VALUES ('R8N7B9', 4, 'Portable Charger X', 99.99, 129.99, 20, 10, 80, 4, null, null);

