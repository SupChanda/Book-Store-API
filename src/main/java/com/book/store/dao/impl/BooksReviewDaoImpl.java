package com.book.store.dao.impl;

import com.book.store.dao.BooksReviewDao;
import com.book.store.models.contract.BooksRequest;
import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.BooksReview;
import com.book.store.models.mappers.BooksReviewMapper;
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
public class BooksReviewDaoImpl extends GenericDaoImpl<BooksReview> implements BooksReviewDao {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    BooksReviewMapper booksReviewMapper;
    private Query query;
    private String queryString;
    private final String queryFromTemplate = "FROM ${Object} b";
    private final String queryWhereTemplate = " WHERE b.${id} =:id";
    Map<String,Object> templateValues = new HashMap<>();
    Map<String,Object> queryParam = new HashMap<>();
    @Override
    public List<BooksReview> getBooksReview(){
        //Map<String,Object> templateValues = new HashMap<>();
        templateValues.put("Object", BooksReview.class.getName());
        templateValues.put("id", BooksReview.Fields.id);
        queryString = generateQueryString(queryFromTemplate,templateValues);

        //String resultSet = "SELECT * FROM Books_Review";
        return (List<BooksReview>) getHQLQueryResultSet(queryString);
    }

    public List<BooksReview> getBooksReviewByBookId(int bookId){

        //Map<String,Object> templateValues = new HashMap<>();
        templateValues.put("Object", BooksReview.class.getName());
        templateValues.put("id", BooksReview.Fields.books + ".id");
        queryString = generateQueryString(queryFromTemplate+queryWhereTemplate,templateValues);

        //Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("id",bookId);

        return (List<BooksReview>) getHQLFullQueryResultSet(queryString,queryParam);
//        String resultSet = "SELECT * FROM Books_Review WHERE BookID =:bookId";
//        query = entityManager.createNativeQuery(resultSet, BooksReview.class);
//        query.setParameter("bookId",bookId);
//        return query.getResultList();

    }

    @Override
    public Object addBookReview(BooksReviewRequest booksReviewRequest, Books books, BookUser bookUser) throws BadRequestException {
        System.out.println("Here 0");
        //CHECK IF THE PERSON HAVE PURCHASED OR RENTED THE BOOK
        String queryMultipleWhereTemplate = " WHERE b.${bookId} = :bookId AND b.${userId} =:userId";
//        templateValues.put("Object", Books.class.getName());
//        templateValues.put("bookId",Books.Fields.id);
//        templateValues.put("userId", BookUser.Fields.id);
//        queryString = generateQueryString(queryFromTemplate + queryMultipleWhereTemplate,templateValues);
//
//        queryParam.put("bookId",booksReviewRequest.getBookId());
//        queryParam.put("userId",booksReviewRequest.getUserId());

//        if(generateQueryString(queryString,queryParam) == null){
//            throw new BadRequestException("The user -> userid = " + booksReviewRequest.getUserId() + " cannot review because the user did not purchase or rent the book -> bookId = " + booksReviewRequest.getBookId());
//        }


        //System.out.println("Here 1");
        //CHECK IF THE PERSON HAS ALREADY REVIEWED THE BOOK
        templateValues.put("Object", BooksReview.class.getName());
        templateValues.put("bookId", BooksReview.Fields.books + ".id");
        templateValues.put("userId", BooksReview.Fields.user + ".id");
        queryString = generateQueryString(queryFromTemplate + queryMultipleWhereTemplate,templateValues);

        queryParam.put("bookId",booksReviewRequest.getBookId());
        queryParam.put("userId",booksReviewRequest.getUserId());
        if((long)getHQLQueryCount(queryString,queryParam) > 0){
            throw new BadRequestException("The user -> UserID = " + booksReviewRequest.getBookId() + " has already reviewed the book -> BookID = " + booksReviewRequest.getBookId());
        }
        // make these book set and user set changes in service layer
        BooksReview booksReview = booksReviewMapper.toBooksReviewFromRequest(booksReviewRequest);
        booksReview.setBooks(books);
        booksReview.setUser(bookUser);
        System.out.println(" bookreview:::::::::::::: " + booksReview);
//        System.out.println(" user " + booksReview.getUser());
        saveOrUpdate(booksReview);

        return "User with id: " + booksReviewRequest.getUserId() + " has added a review for book id: " + booksReviewRequest.getBookId();


    }
}
