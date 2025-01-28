package com.book.store.service;

import com.book.store.models.domain.Books;
import com.book.store.models.domain.Books_Purchased;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksPurchaseService {
    List<Books_Purchased> getPurchasedBooksDetails() throws BadRequestException;
    String addBookPurchasedOrRentDetails(String title,String transactionType,int quantity,String currentUser) throws BadRequestException;

    String UpdateBookDetailsOnReturn(int bookId, int userId) throws BadRequestException;
}
