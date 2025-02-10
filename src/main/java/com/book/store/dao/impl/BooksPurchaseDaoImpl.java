package com.book.store.dao.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.dao.BooksPurchaseDao;
import com.book.store.models.domain.BooksPurchased;
import com.book.store.models.domain.Books;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
@Transactional
public class BooksPurchaseDaoImpl implements BooksPurchaseDao {

    @Autowired
    BooksRepository booksRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final Date purchasedDate = Date.valueOf(LocalDate.now());
    private Date rentalStartDate = Date.valueOf((LocalDate.now()));
    private final Date rentalEndDate    = Date.valueOf(LocalDate.now());
    Date today =  Date.valueOf(LocalDate.now());
    private Query queryInsert;
    private Query queryUpdate;
    private Query query;
    public BooksPurchaseDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public void setParameter(Query query, int bookId,int userId,String transactionType,int quantity,float purchasedPrice,float rentalFeeAccrued){
        boolean purchased = transactionType.equalsIgnoreCase("Purchased");
        float defaultValue = 0.0f;
        query.setParameter(1, bookId);
        query.setParameter(2, userId);
        query.setParameter(3, transactionType);
        if(purchased){
            query.setParameter(4, purchasedDate);
            query.setParameter(5, null);
            query.setParameter(6, null);
            query.setParameter(8, purchasedPrice);
            query.setParameter(9, defaultValue);
        }else{
            query.setParameter(4, null);
            query.setParameter(5, rentalStartDate);
            query.setParameter(6, null);
            query.setParameter(8, defaultValue);
            query.setParameter(9, rentalFeeAccrued);
        }
        query.setParameter(7, quantity);
    }
    public List<BooksPurchased> getAllPurchasedBooksDetails(){
        String queryString = "SELECT b FROM BooksPurchased b";
        query = entityManager.createQuery(queryString, BooksPurchased.class);
        return query.getResultList();
    }
    public void addBookPurchasedOrRentDetails(int bookId,int userId,String transactionType,int quantity,float purchasedPrice,float rentalFeeAccrued) throws BadRequestException {
        try {

            //CHECK HOW MANY BOOKS ARE LEFT
            String bookRemainingQuery = "SELECT NoOfCopies,Title FROM Book_Record WHERE ID = :bookId";
            query = entityManager.createNativeQuery(bookRemainingQuery);
            query.setParameter("bookId", bookId);
            Object[] result = (Object[]) query.getSingleResult();
            if((int) result[0] < quantity){
                String BookTitle = (String) result[1];
                throw new BadRequestException(" , unfortunately there are only "+ (int) result[0] + " '" +  BookTitle + "' book(s) is(are) left");
            }

            //Checking whether the member has already rented 2 books
            if(transactionType.equalsIgnoreCase("Rented")) {
                String quantityCheckQuery = "SELECT COUNT(*) FROM Books_Purchased WHERE UserId = :userId AND RentalEndDate IS NULL AND TransactionType = :transactionType"; //totalBookRentedQueryString
                query = entityManager.createNativeQuery(quantityCheckQuery);
                query.setParameter("userId", userId);
                query.setParameter("transactionType", "Rented");
                int quantityCheck = (int) query.getSingleResult();
//                if (query.getSingleResult() == null) { // get single result in one query
//                    quantityCheck = 0;
//                } else {
//                    quantityCheck = (int) query.getSingleResult();
//                }
                System.out.println("quantityCheck" + quantityCheck);
                if (quantityCheck > 1) {
                    throw new BadRequestException(" can only rent 2 books at a time");
                }
            }
            //Checking whether the user has active membership

            String isActiveMember = "SELECT IsActiveMember FROM Book_User WHERE ID = :userId"; //totalBookRentedQueryString
            query = entityManager.createNativeQuery(isActiveMember);
            query.setParameter("userId",userId);
            System.out.println("user id: " + userId + " is Active Member " + query.getSingleResult());
            if ( !((boolean) query.getSingleResult())) {
                throw new BadRequestException(" is not an active Member");
            }

//            if(booksRepository.findById(bookId).isPresent()){
//                noOfCopies = booksRepository.findById(bookId).get().getNoOfCopies();
//            }
            int noOfCopies = booksRepository.findById(bookId).get().getNoOfCopies();
            String insertBookRecordSql = "INSERT INTO Books_Purchased" +
                    "(BookID,UserID,TransactionType,PurchasedDate,RentalStartDate,RentalEndDate,Quantity,PurchasedPrice,RentalFeeAccrued) " +
                    "VALUES(?,?,?,?,?,?,?,?,?)";
            String UpdateBookRecordSql = "UPDATE Book_Record SET noOfCopies = :noOfCopies WHERE ID = :ID";

            queryInsert = entityManager.createNativeQuery(insertBookRecordSql);
            setParameter(queryInsert,bookId,userId,transactionType,quantity,purchasedPrice*quantity,rentalFeeAccrued*quantity);
            queryInsert.executeUpdate();

            queryUpdate = entityManager.createNativeQuery(UpdateBookRecordSql);
            queryUpdate.setParameter("noOfCopies", noOfCopies- quantity);
            queryUpdate.setParameter("ID", bookId);
            queryUpdate.executeUpdate();
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        //return List.of(new Books_Purchased());
    }


    public void UpdateBookDetailsOnReturn(int bookId, int userId) throws BadRequestException {
        try {


            String queryString;
            float extraRentalFee = 0.00f;
            float rentalFeeAccrued = 0.00f;
            int quantity = 0;
            String whereClause = "WHERE BookID = :bookId AND UserID = :userId AND TransactionType = :transactionType";
            //System.out.println("yes here");

            //UPDATE RETURNED RENTED BOOK PURCHASE RECORD ONLY AND REJECT IF THE RETURNING BOOK IS PURCHASED

            queryString = "SELECT RentalStartDate,RentalFeeAccrued,Quantity,TransactionType FROM Books_Purchased " + whereClause; //b.RentalStartDate,b.RentalFeeAccrued
            Query query = entityManager.createNativeQuery(queryString);
            query.setParameter("bookId", bookId);
            query.setParameter("userId", userId);
            query.setParameter("transactionType", "Rented");
            List<Object[]> resultSet = query.getResultList();
            for (Object[] res : resultSet) {
                //System.out.println("here " + res[3]);
                if(res[3].equals("Purchased")){
                    throw new BadRequestException("The book cannot be returned as it is purchased and not rented.");
                }
                rentalStartDate = (Date) res[0];
                rentalFeeAccrued = ((Number) res[1]).floatValue();
                quantity = (int) (res[2]);
            }
            //System.out.println("Rental Start date: " + rentalStartDate + " extraRentalFee: " + extraRentalFee);

            long datediff = ChronoUnit.DAYS.between(rentalStartDate.toLocalDate(), today.toLocalDate());
            System.out.println("Period: " + datediff);
            if (datediff > 30) {
                extraRentalFee = rentalFeeAccrued + datediff - 30;
            }

            //System.out.println("extraRentalFee: " + extraRentalFee);

            queryString = "UPDATE Books_Purchased  SET RentalEndDate = :rentalEndDate, RentalFeeAccrued = :rentalFeeAccrued " + whereClause;
            query = entityManager.createNativeQuery(queryString);
            query.setParameter("rentalEndDate", rentalEndDate);
            query.setParameter("rentalFeeAccrued", extraRentalFee);
            query.setParameter("bookId", bookId);
            query.setParameter("userId", userId);
            query.setParameter("transactionType", "Rented");
            query.executeUpdate();

            //UPDATE BOOK COPIES RECORD
            int noOfCopies = 0;
            queryString = "SELECT NoOfCopies FROM Book_Record WHERE ID = :bookId ";
            query = entityManager.createNativeQuery(queryString);
            query.setParameter("bookId", bookId);
            System.out.println("yes here: ");
            List<Object> resultSet1 = query.getResultList();
            for (Object res : resultSet1) {
                noOfCopies = (int) res;
            }
            System.out.println("no of copies: " + noOfCopies);
            queryString = "UPDATE Book_Record  SET NoOfCopies = :noOfCopies WHERE ID = :bookId";
            query = entityManager.createNativeQuery(queryString);
            query.setParameter("bookId", bookId);
            query.setParameter("noOfCopies", quantity + noOfCopies);
            query.executeUpdate();
        }
        catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}
