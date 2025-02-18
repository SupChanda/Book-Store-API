DROP TABLE IF EXISTS Books
CREATE TABLE Books
(
    ID                      INT NOT NULL IDENTITY(0,1) PRIMARY KEY
    ,Title                  VARCHAR(80) NOT NULL UNIQUE
    ,Author                 VARCHAR(80) NOT NULL
    ,Genre                  VARCHAR(80) NOT NULL
    ,[Price]                DECIMAL(10,2)
    ,[RentalFee]            DECIMAL(10,2)
    ,[NoOfCopies]           INT     --Increment or decrement depending on availability
)
