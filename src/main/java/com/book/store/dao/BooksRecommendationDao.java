package com.book.store.dao;

import com.book.store.models.domain.Books;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksRecommendationDao {
    List<Books> getBooksRecommendation(int userId) throws BadRequestException;
}
