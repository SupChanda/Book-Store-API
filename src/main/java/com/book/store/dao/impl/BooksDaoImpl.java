package com.book.store.dao.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.dao.BooksDao;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.Books_Purchased;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import java.sql.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class BooksDaoImpl implements BooksDao {

    @Autowired
    BooksRepository booksRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final Date purchasedDate = Date.valueOf(LocalDate.now());
    private final Date rentalStartDate = Date.valueOf(LocalDate.now());
    private final Date rentalEndDate    = null ; //Date.valueOf(LocalDate.now());
    private Query queryInsert;
    private Query queryUpdate;
    private Query query;
    public BooksDaoImpl(EntityManager entityManager) {
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
            query.setParameter(6, rentalEndDate);
            query.setParameter(8, defaultValue);
            query.setParameter(9, rentalFeeAccrued);
        }

        query.setParameter(7, quantity);
    }

    public void addBookPurchasedOrRentDetails(int bookId,int userId,String transactionType,int quantity,float purchasedPrice,float rentalFeeAccrued) throws BadRequestException {
        try {
            int noOfCopies = booksRepository.findById(bookId).get().getNoOfCopies();
            String insertBookRecordSql = "INSERT INTO Books_Purchased" +
                    "(BookID,UserID,TransactionType,PurchasedDate,RentalStartDate,RentalEndDate,Quantity,PurchasedPrice,RentalFeeAccrued) " +
                    "VALUES(?,?,?,?,?,?,?,?,?)";
            String UpdateBookRecordSql = "UPDATE Book_Record SET noOfCopies = :noOfCopies WHERE ID = :ID";

            queryInsert = entityManager.createNativeQuery(insertBookRecordSql);
            setParameter(queryInsert,bookId,userId,transactionType,quantity,purchasedPrice,rentalFeeAccrued);
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
    public List<Books_Purchased> getAllPurchasedBooksDetails(){
        String queryString = "SELECT b FROM Books_Purchased b";
        query = entityManager.createQuery(queryString, Books_Purchased.class);
        return query.getResultList();
    }

//    public List<Books_Purchased> getPurchasedBooksDetails(){
//        String queryString = "SELECT b FROM Books_Purchased b WHERE ID = :?";
//        query = entityManager.createQuery(queryString, Books_Purchased.class);
//        query.setParameter(1,)
//        return query.getResultList();
//    }
}
