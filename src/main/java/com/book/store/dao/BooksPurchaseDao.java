package com.book.store.dao;

import com.book.store.models.domain.*;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.BooksPurchasedDTO;
import com.book.store.models.dto.UserDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksPurchaseDao {

    Object getAllPurchasedBooksDetails();
    BooksPurchased getPurchasedBooksDetailsById(int id) throws BadRequestException;
    Object getPurchasedBooksDetailsByUserIdAndBookId(int userId,int bookId);
    void addBookPurchasedOrRentDetails(BooksDTO booksDTO, UserDTO userDTO,String transactionType,int quantity) throws BadRequestException;
    void UpdateBookDetailsOnReturn(BooksPurchasedDTO booksPurchasedDTO) throws BadRequestException;
}
