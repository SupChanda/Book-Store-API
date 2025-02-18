package com.book.store.service.impl;

import com.book.store.dao.BooksReviewDao;
import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BooksReview;
import com.book.store.models.mappers.BooksReviewMapper;
import com.book.store.service.BooksReviewService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    @Autowired
    BooksReviewMapper booksReviewMapper;

    private final Date dateReviewed = Date.valueOf(LocalDate.now());
    @Override
    public BooksReview addBooksReview(int bookId, int userId, String comments) throws BadRequestException {
        try{
            return booksReviewDao.addBookReview(bookId,userId,dateReviewed, comments);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public Object getBooksReview() throws BadRequestException {
        try{
            return booksReviewMapper.toBooksReviewRequestListFromRequest(booksReviewDao.getBooksReview());
            //return booksReviewDao.getBooksReview(); //booksReviewRequest; //booksReviewDao.getBooksReview();
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public List<BooksReview> getBooksReviewByID(int bookId) throws BadRequestException {
        try{
            return booksReviewDao.getBooksReviewById(bookId);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}
