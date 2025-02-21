package com.book.store.dao;

import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.BooksReview;
import org.apache.coyote.BadRequestException;

import java.awt.print.Book;
import java.sql.Date;
import java.util.List;

public interface BooksReviewDao {
    Object addBookReview(BooksReviewRequest booksReviewRequest, Books books, BookUser bookUser) throws BadRequestException;
    List<BooksReview> getBooksReview();
    List<BooksReview> getBooksReviewById(int BookId);
}
