package com.book.store.service;

import com.book.store.models.contract.BooksPurchasedRequest;
import com.book.store.models.domain.BooksPurchased;
import com.book.store.models.dto.BooksPurchasedDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksPurchaseService {
    Object getPurchasedBooksDetails() throws BadRequestException;
    Object getPurchasedBooksDetailsById(int id) throws BadRequestException;
    String addBookPurchasedOrRentDetails(BooksPurchasedRequest booksPurchasedRequest) throws BadRequestException;
    Object getPurchasedBooksDetailsByUserIdAndBookId(int userId, int bookId) throws BadRequestException;
    String UpdateBookDetailsOnReturn(BooksPurchasedDTO booksPurchasedDTO) throws BadRequestException;
}
