DROP TABLE IF EXISTS Book_Record
CREATE TABLE Book_Record
(
    ID                      INT NOT NULL IDENTITY(0,1)
    ,Title                  VARCHAR(80) NOT NULL UNIQUE
    ,Author                 VARCHAR(80) NOT NULL
    ,Genre                  VARCHAR(80) NOT NULL
    ,[Price]                DECIMAL(10,2)
    ,[RentalFee]            DECIMAL(10,2)
    ,[NoOfCopies]           INT     --Increment or decrement depending on availability

)
--Book_transaction
--,AssignedTo           VARCHAR(80) DEFAULT NULL
    --,TransactionType      VARCHAR(10) CHECK (TransactionType IN ('Available','Rent','Purchase'))
    --,ReturnDate           DATE
    --,CONSTRAINT FK_BookRecord_AssignedTo  FOREIGN KEY (AssignedTo)   REFERENCES  UserRecord(UserName)