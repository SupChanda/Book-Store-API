package com.book.store.dao;

import com.book.store.models.domain.*;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksPurchaseDao {

    Object getAllPurchasedBooksDetails();
    Object getPurchasedBooksDetailsById(int id);
    void addBookPurchasedOrRentDetails(BooksDTO booksDTO, UserDTO userDTO,String transactionType,int quantity) throws BadRequestException;
    void UpdateBookDetailsOnReturn(int bookReturnId, int userId) throws BadRequestException;
}
