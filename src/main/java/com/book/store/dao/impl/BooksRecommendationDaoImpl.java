package com.book.store.dao.impl;

import com.book.store.dao.BooksRecommendationDao;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.BooksPurchased;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BooksRecommendationDaoImpl extends GenericDaoImpl<Books> implements BooksRecommendationDao {

    private String queryFromTemplate = "FROM ${object} b ";

    private String queryString;
    Map<String,Object> templateValues = new HashMap<>();
    Map<String,Object> queryParams = new HashMap<>();
    Map<String, Object> GenreVsCount = new HashMap<>();
    @Override
    public List<Books> getBooksRecommendation(int userId) throws BadRequestException {
        List<Books> bookList = new ArrayList<>();
        try {
            //when user has no books, send some top selling books
            System.out.println("userId " + userId);
            String GenrePurchasedQuery = "SELECT bk.${column1},COUNT(bk.${column2}) "
                    + queryFromTemplate
                    + "JOIN ${books} bk ON bk.${id} = b.${bookId} "
                    + "WHERE b.${userId} = :userId "
                    + "GROUP BY bk.${groupBy} "
                    + "ORDER BY COUNT(bk.${orderBy1}) DESC, bk.${orderBy2}";
            templateValues.put("column1", Books.Fields.genre);
            templateValues.put("column2", Books.Fields.genre);
            templateValues.put("object", BooksPurchased.class.getName());
            templateValues.put("books", Books.class.getName());
            templateValues.put("id", Books.Fields.id);
            templateValues.put("bookId", BooksPurchased.Fields.books + ".id");
            templateValues.put("userId", BooksPurchased.Fields.user + ".id" );
            templateValues.put("groupBy", Books.Fields.genre);
            templateValues.put("orderBy1", Books.Fields.genre);
            templateValues.put("orderBy2", Books.Fields.genre);
            queryString = generateQueryString(GenrePurchasedQuery,templateValues);

            queryParams.put("userId",userId);
            List<Object> resultList = (List<Object>) getHQLFullQueryResultSet(queryString,queryParams);

            if ( resultList == null) {
                throw new BadRequestException(" User with userid " + userId + " have not yet bought a book");
            }
            queryParams.clear();
            for (Object obj : resultList) {

                String genre = (String) ((Object[]) obj)[0];
                Long count = (Long) ((Object[]) obj)[1];
                GenreVsCount.put(genre, count);
            }

            String recommendedBookListQuery = "FROM ${books} b WHERE b.${genre} = :genre ORDER BY b.${title}";
            templateValues.put("books",Books.class.getName());
            templateValues.put("genre",Books.Fields.genre);
            templateValues.put("title",Books.Fields.title);
            queryString = generateQueryString(recommendedBookListQuery,templateValues);


            for (String genre : GenreVsCount.keySet()) {
                queryParams.put("genre",genre);
                List<Books> genreList = (List<Books>)getHQLFullQueryResultSet(queryString,queryParams);
                for(Object obj: genreList){
                    Books itemSet = (Books) obj;
                    bookList.add(itemSet);
                }
            }
        }catch (Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        return bookList;
    }
}
