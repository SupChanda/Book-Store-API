--Creating a Book record table. UserID is an identity key, Title is an unique key,UserID is a foreign key to UserRecord:ID column
DROP TABLE IF EXISTS BookRecord
CREATE TABLE BookRecord
(
    ID                      INT NOT NULL IDENTITY(0,1)
    ,UserID                 INT NOT NULL
    ,Title                  VARCHAR(80) NOT NULL UNIQUE
    ,Author                 VARCHAR(80) NOT NULL
    ,Genre                  VARCHAR(80) NOT NULL
    ,TransactionType        VARCHAR(10) CHECK (TransactionType IN ('Available','Rent','Purchase'))
    ,ReturnDate             DATE
    ,[Price($)]             DECIMAL(10,2)
    ,[RentalFee($)]         DECIMAL(10,2)
    ,CONSTRAINT FK_BookRecord_UserID   FOREIGN KEY (UserID)   REFERENCES  UserRecord(ID)
)