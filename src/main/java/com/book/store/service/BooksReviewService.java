package com.book.store.service;

import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BooksReview;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksReviewService {

    String addBooksReview(BooksReviewRequest booksReviewRequest) throws BadRequestException;
    Object getBooksReview() throws BadRequestException;
    List<BooksReview> getBooksReviewByBookId(int bookId) throws  BadRequestException;
}
