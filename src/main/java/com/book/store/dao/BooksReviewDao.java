package com.book.store.dao;

import com.book.store.models.domain.Books_Review;
import org.apache.coyote.BadRequestException;

import java.sql.Date;
import java.util.List;

public interface BooksReviewDao {
    Books_Review addBookReview(int bookId, int userId, Date dateReviewed,String comments) throws BadRequestException;
    List<Books_Review> getBooksReview();
}
