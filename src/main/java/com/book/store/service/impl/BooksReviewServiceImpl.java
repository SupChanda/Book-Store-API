package com.book.store.service.impl;

import com.book.store.dao.BooksReviewDao;
import com.book.store.models.domain.Books_Review;
import com.book.store.service.BooksReviewService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class BooksReviewServiceImpl implements BooksReviewService {
    @Autowired
    BooksReviewDao booksReviewDao;
    private final Date dateReviewed = Date.valueOf(LocalDate.now());
    @Override
    public Books_Review addBooksReview(int bookId, int userId, String comments) throws BadRequestException {
        try{
            return booksReviewDao.addBookReview(bookId,userId,dateReviewed, comments);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public List<Books_Review> getBooksReview() throws BadRequestException {
        try{
            return booksReviewDao.getBooksReview();
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public List<Books_Review> getBooksReviewByID(int bookId) throws BadRequestException {
        try{
            return booksReviewDao.getBooksReviewById(bookId);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}
