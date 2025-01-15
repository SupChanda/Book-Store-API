--Creating a User record table. ID is an primary and identity key and Username is an unique key
DROP TABLE IF EXISTS UserRecord
CREATE TABLE UserRecord
(
    ID INT NOT NULL IDENTITY(0,1) PRIMARY KEY
    ,UserName VARCHAR(80)   NOT NULL
    ,Password VARCHAR(80)   NOT NULL
    ,FirstName  VARCHAR(80) NOT NULL
    ,LastName VARCHAR(80)   NOT NULL
    ,IsAdmin  BIT           NOT NULL
)