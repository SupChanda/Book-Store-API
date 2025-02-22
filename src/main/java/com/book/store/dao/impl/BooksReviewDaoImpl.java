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
    @Autowired
    BooksReviewMapper booksReviewMapper;
    private String queryString;
    private final String queryFromTemplate = "FROM ${Object} b";
    private final String queryWhereTemplate = " WHERE b.${id} =:id";
    Map<String,Object> templateValues;
    Map<String,Object> queryParam;
    @Override
    public List<BooksReview> getBooksReview(){
        templateValues = new HashMap<>();
        templateValues.put("Object", BooksReview.class.getName());
        templateValues.put("id", BooksReview.Fields.id);
        queryString = generateQueryString(queryFromTemplate,templateValues);

        return (List<BooksReview>) getHQLQueryResultSet(queryString);
    }

    public List<BooksReview> getBooksReviewByBookId(int bookId){
        templateValues = new HashMap<>();
        templateValues.put("Object", BooksReview.class.getName());
        templateValues.put("id", BooksReview.Fields.books + ".id");
        queryString = generateQueryString(queryFromTemplate+queryWhereTemplate,templateValues);

        queryParam = new HashMap<>();
        queryParam.put("id",bookId);

        return (List<BooksReview>) getHQLFullQueryResultSet(queryString,queryParam);
    }

    @Override
    public Object addBookReview(BooksReviewRequest booksReviewRequest, Books books, BookUser bookUser) throws BadRequestException {
        System.out.println("Here 0");
        //CHECK IF THE PERSON HAVE PURCHASED OR RENTED THE BOOK
        String queryMultipleWhereTemplate = " WHERE b.${bookId} = :bookId AND b.${userId} =:userId";


        //CHECK IF THE PERSON HAS ALREADY REVIEWED THE BOOK
        templateValues = new HashMap<>();
        templateValues.put("Object", BooksReview.class.getName());
        templateValues.put("bookId", BooksReview.Fields.books + ".id");
        templateValues.put("userId", BooksReview.Fields.user + ".id");
        queryString = generateQueryString(queryFromTemplate + queryMultipleWhereTemplate,templateValues);

        queryParam = new HashMap<>();
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
        saveOrUpdate(booksReview);

        return "User with id: " + booksReviewRequest.getUserId() + " has added a review for book id: " + booksReviewRequest.getBookId();


    }
}
