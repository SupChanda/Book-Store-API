package com.book.store.dao;

import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BooksReview;
import org.apache.coyote.BadRequestException;

import java.sql.Date;
import java.util.List;

public interface BooksReviewDao {
    BooksReview addBookReview(int bookId, int userId, Date dateReviewed, String comments) throws BadRequestException;
    Object getBooksReview();
    List<BooksReview> getBooksReviewById(int BookId);
}
