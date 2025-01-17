package com.book.store.service.impl;

import com.book.store.models.domain.Books;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
public interface BooksManagementService {
    List<Books> getBooks();
    String createBooks(Books bk, String admin);
    String updateBooks(String title,Books bk, String admin);
    String deleteBooks(String title, String admin);
}
