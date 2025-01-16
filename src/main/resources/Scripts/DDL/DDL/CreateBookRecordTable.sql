DROP TABLE IF EXISTS BookRecord
CREATE TABLE BookRecord
(
    ID                      INT NOT NULL IDENTITY(0,1)
    ,AssignedTo             VARCHAR(80) DEFAULT NULL
    ,Title                  VARCHAR(80) NOT NULL UNIQUE
    ,Author                 VARCHAR(80) NOT NULL
    ,Genre                  VARCHAR(80) NOT NULL
    ,TransactionType        VARCHAR(10) CHECK (TransactionType IN ('Available','Rent','Purchase'))
    ,ReturnDate             DATE
    ,[Price]                DECIMAL(10,2)
    ,[RentalFee]            DECIMAL(10,2)
    ,[CreatedBy]            VARCHAR(80) NOT NULL
    ,CONSTRAINT FK_BookRecord_AssignedTo  FOREIGN KEY (AssignedTo)   REFERENCES  UserRecord(UserName)
)