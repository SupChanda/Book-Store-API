package com.book.store.service;

import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
public interface BooksManagementService {
    List<BooksDTO> getBooks() throws BadRequestException;
    String createBooks(Books bk, String admin);
    Books getBookByTitle(String title) throws BadRequestException;
    String updateBooks(String title,Books bk, String currentUser);
    String deleteBooks(String title, String currentUser);


}
