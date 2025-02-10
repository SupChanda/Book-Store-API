package com.book.store.service;

import com.book.store.models.domain.BooksPurchased;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksPurchaseService {
    List<BooksPurchased> getPurchasedBooksDetails() throws BadRequestException;
    String addBookPurchasedOrRentDetails(String title,String transactionType,int quantity,String currentUser) throws BadRequestException;

    String UpdateBookDetailsOnReturn(int bookId, int userId) throws BadRequestException;
}
