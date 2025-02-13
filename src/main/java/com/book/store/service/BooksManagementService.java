package com.book.store.service;

import com.book.store.models.contract.BooksRequest;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
public interface BooksManagementService {
    List<BooksDTO> getBooks() throws BadRequestException;
    Object getBooksByIdOrName(Object obj) throws BadRequestException;

    String createBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException;

    String updateBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException;
    String deleteBooks(BooksRequest booksRequest, String currentUser) throws BadRequestException;


}
