package com.book.store.dao.impl;

import com.book.store.dao.BooksReviewDao;
import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BooksReview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class BooksReviewDaoImpl extends GenericDaoImpl<BooksReview> implements BooksReviewDao {
    @PersistenceContext
    private EntityManager entityManager;
    private Query query;
    private String queryString;
    private final String queryFromTemplate = "FROM ${Object} b";
    private final String queryWhereTemplate = "WHERE b.${id} =:id";

    @Override
    public Object getBooksReview(){
        Map<String,Object> templateValues = new HashMap<>();
        templateValues.put("Object", BooksReviewRequest.class.getName());
        templateValues.put("id", BooksReviewRequest.Fields.id);
        queryString = generateQueryString(queryFromTemplate,templateValues);

        //String resultSet = "SELECT * FROM Books_Review";
        return getHQLQueryResultSet(queryString);
    }

    public List<BooksReview> getBooksReviewById(int bookId){
        String resultSet = "SELECT * FROM Books_Review WHERE BookID =:bookId";
        query = entityManager.createNativeQuery(resultSet, BooksReview.class);
        query.setParameter("bookId",bookId);
        return query.getResultList();

    }

    @Override
    public BooksReview addBookReview(int bookId, int userId, Date dateReviewed, String comments) throws BadRequestException {
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
        query = entityManager.createNativeQuery(resultSet, BooksReview.class);
        query.setParameter("bookId",bookId);
        query.setParameter("userId",userId);

        return (BooksReview) query.getSingleResult();


    }
}
