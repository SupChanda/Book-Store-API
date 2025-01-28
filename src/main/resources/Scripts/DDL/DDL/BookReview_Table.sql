DROP TABLE IF EXISTS Books_Review
CREATE TABLE Books_Review
(
    ID              INT     IDENTITY(0,1) PRIMARY KEY
    ,BookID         INT     NOT NULL
    ,UserID         INT     NOT NULL
    ,DateReviewed   DATE    NOT NULL
    ,Comments       NVARCHAR(MAX)
)