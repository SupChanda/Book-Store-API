DROP TABLE IF EXISTS BooksReview
CREATE TABLE BooksReview
(
    ID              INT     IDENTITY(0,1) PRIMARY KEY
    ,BookID         INT     NOT NULL
    ,UserID         INT     NOT NULL
    ,DateReviewed   DATE    NOT NULL
    ,Comments       NVARCHAR(MAX)
)