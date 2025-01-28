package com.book.store.dao.impl;

import com.book.store.dao.BooksReviewDao;
import com.book.store.models.domain.Books_Purchased;
import com.book.store.models.domain.Books_Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@Transactional
public class BooksReviewDaoImpl implements BooksReviewDao {
    @PersistenceContext
    private EntityManager entityManager;
    private Query query;
    @Override
    public List<Books_Review> getBooksReview(){
        String resultSet = "SELECT * FROM Books_Review";
        query = entityManager.createNativeQuery(resultSet, Books_Review.class);
        return query.getResultList();
    }

    public List<Books_Review> getBooksReviewById(int bookId){
        String resultSet = "SELECT * FROM Books_Review WHERE BookID =:bookId";
        query = entityManager.createNativeQuery(resultSet, Books_Review.class);
        query.setParameter("bookId",bookId);
        return query.getResultList();

    }

    @Override
    public Books_Review addBookReview(int bookId, int userId, Date dateReviewed, String comments) throws BadRequestException {
        System.out.println("Here 0");
        //CHECK IF THE PERSON HAVE PURCHASED OR RENTED THE BOOK
        String checkBookAndUserIDBeforeReviewing = "SELECT COUNT(*) FROM Books_Purchased WHERE BookID = :bookId AND UserID = :userId";
        query = entityManager.createNativeQuery(checkBookAndUserIDBeforeReviewing);
        //System.out.println("Here again");
        query.setParameter("bookId",bookId);
        query.setParameter("userId",userId);

        if(query.getSingleResult()== null){
            throw new BadRequestException("The user -> userid = " + userId + " cannot review because the user did not purchase or rent the book -> bookId = " + bookId);
        }
        //System.out.println("Here 1");
        //CHECK IF THE PERSON HAS ALREADY REVIEWED THE BOOK
        String checkIfBookReviewedAlready = "SELECT COUNT(*) FROM Books_Review WHERE BookID = :bookId AND UserID = :userId";
        query = entityManager.createNativeQuery(checkIfBookReviewedAlready);
        query.setParameter("bookId",bookId);
        query.setParameter("userId",userId);
        if((int) query.getSingleResult()== 1){
            throw new BadRequestException("The user -> UserID = " + userId + " has already reviewed the book -> BookID = " + bookId);
        }
        //System.out.println("Here 2");
        String addBookReviewQuery = "INSERT INTO Books_Review (BookID,UserID,DateReviewed,Comments) VALUES(?,?,?,?)";
        query = entityManager.createNativeQuery(addBookReviewQuery);
        query.setParameter(1,bookId);
        query.setParameter(2,userId);
        query.setParameter(3,dateReviewed);
        query.setParameter(4,comments);
        query.executeUpdate();

        //System.out.println("Here 3");
        String resultSet = "SELECT * FROM Books_Review WHERE BookID = :bookId AND UserID = :userId";
        query = entityManager.createNativeQuery(resultSet, Books_Review.class);
        query.setParameter("bookId",bookId);
        query.setParameter("userId",userId);

        return (Books_Review) query.getSingleResult();


    }
}
