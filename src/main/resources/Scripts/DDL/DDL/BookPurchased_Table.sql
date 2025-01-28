DROP TABLE IF EXISTS Book_Purchased
CREATE TABLE Book_Purchased
(
   ID                   INT     IDENTITY(0,1) PRIMARY KEY
   ,BookID              INT     NOT NULL
   ,UserID              INT     NOT NULL
   ,TransactionType     VARCHAR(10) CHECK (TransactionType IN ('Purchased','Rented'))
   ,PurchasedDate       DATE    DEFAULT NULL
   ,RentalStartDate     DATE    DEFAULT NULL
   ,RentalEndDate       DATE    DEFAULT NULL
   ,Quantity            INT     NOT NULL
   ,PurchasedPrice      DECIMAL(10,2)
   ,RentalFeeAccrued    DECIMAL(10,2)

   FOREIGN KEY (BookID) REFERENCES Book_Record(ID),
   FOREIGN KEY (UserID) REFERENCES Book_User(ID)
)