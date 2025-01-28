package com.book.store.service;

import com.book.store.models.domain.Books_Review;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksReviewService {

    Books_Review addBooksReview(int bookId, int userId, String comments) throws BadRequestException;
    List<Books_Review> getBooksReview() throws BadRequestException;
    List<Books_Review> getBooksReviewByID(int bookId) throws  BadRequestException;
}
