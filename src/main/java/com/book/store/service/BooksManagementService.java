package com.book.store.service;

import com.book.store.models.domain.Books;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
public interface BooksManagementService {
    List<Books> getBooks();
    String createBooks(Books bk, String admin);
    Books getBookByTitle(String title) throws BadRequestException;
    String updateBooks(String title,Books bk, String currentUser);
    String deleteBooks(String title, String currentUser);
}
