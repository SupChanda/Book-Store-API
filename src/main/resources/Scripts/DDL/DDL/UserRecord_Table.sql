--Creating a User table for Book Management API. ID is an primary and identity key and Username is an unique key
DROP TABLE IF EXISTS Book_User
CREATE TABLE Book_User
(
    ID                  INT             NOT NULL IDENTITY(0,1) PRIMARY KEY
    ,UserName           VARCHAR(80)     NOT NULL UNIQUE
    ,Password           VARCHAR(80)     NOT NULL
    ,FirstName          VARCHAR(80)     NOT NULL
    ,LastName           VARCHAR(80)     NOT NULL
    ,IsActiveMember     BIT             NOT NULL
    ,IsAdmin            BIT             NOT NULL
)