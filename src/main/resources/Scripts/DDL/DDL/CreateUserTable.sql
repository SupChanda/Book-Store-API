--Creating a User record table
DROP TABLE IF EXISTS UserRecord
CREATE TABLE UserRecord
(
    ID INT NOT NULL IDENTITY(0,1)
    ,UserName VARCHAR(80)
    ,Password VARCHAR(80)
    ,FirstName  VARCHAR(80)
    ,LastName VARCHAR(80)
    ,IsAdmin  BIT
)