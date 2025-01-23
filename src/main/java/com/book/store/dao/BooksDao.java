package com.book.store.dao;

import com.book.store.models.domain.*;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksDao {

    void addBookPurchasedOrRentDetails(int bookId,int userId,String transactionType,int quantity,float purchasedPrice,float rentalFeeAccrued) throws BadRequestException;
    List<Books_Purchased> getAllPurchasedBooksDetails();
}
