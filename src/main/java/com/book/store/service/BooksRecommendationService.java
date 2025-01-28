package com.book.store.service;

import com.book.store.models.domain.Books;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksRecommendationService {
    List<Books> getBooksRecommendation(int userId) throws BadRequestException;
}
