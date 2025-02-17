package com.book.store.service;

import com.book.store.models.domain.BooksReview;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksReviewService {

    BooksReview addBooksReview(int bookId, int userId, String comments) throws BadRequestException;
    Object getBooksReview() throws BadRequestException;
    List<BooksReview> getBooksReviewByID(int bookId) throws  BadRequestException;
}
