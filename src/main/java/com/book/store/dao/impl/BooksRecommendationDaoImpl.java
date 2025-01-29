package com.book.store.dao.impl;

import com.book.store.dao.BooksRecommendationDao;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.Books_Review;
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
public class BooksRecommendationDaoImpl implements BooksRecommendationDao {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<Books> getBooksRecommendation(int userId) throws BadRequestException {
        List<Books> bookList = new ArrayList<>();
        try {
            Query query;
            System.out.println("userId " + userId);
            String top3GenrePurchased = "SELECT TOP 2 BK.Genre,COUNT(BK.Genre) FROM Books_Purchased BP " +
                    "JOIN Book_Record BK ON BK.ID = BP.BookID " +
                    "WHERE UserID = :userId " +
                    "GROUP BY BK.Genre " +
                    "ORDER BY COUNT(BK.Genre) DESC,BK.Genre";
            query = entityManager.createNativeQuery(top3GenrePurchased);
            query.setParameter("userId", userId);
            Map<String, Integer> GenreVsCount = new HashMap<>();
            if (query.getResultList() == null) {
                throw new BadRequestException(" User with userid " + userId + " have not yet bought a book");
            }
            for (Object obj : query.getResultList()) {
                String genre = (String) ((Object[]) obj)[0];
                int count = (int) ((Object[]) obj)[1];
                GenreVsCount.put(genre, count);
            }

            String recommendedBookList = "SELECT TOP 2 * FROM Book_Record WHERE Genre = :genre ORDER BY Title";
            query = entityManager.createNativeQuery(recommendedBookList, Books.class);
            for (String genre : GenreVsCount.keySet()) {
                System.out.println("genre is "+ genre);
                query.setParameter("genre", genre);
                for(Object obj: query.getResultList()){
                    Books itemSet = (Books) obj;
                    bookList.add(itemSet);
                }

            }
        }catch (Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        return bookList;



        //return  null;
    }
}
