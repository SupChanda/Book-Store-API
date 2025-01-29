package com.book.store.dao;

import com.book.store.models.domain.*;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BooksPurchaseDao {

    List<Books_Purchased> getAllPurchasedBooksDetails();
    void addBookPurchasedOrRentDetails(int bookId,int userId,String transactionType,int quantity,float purchasedPrice,float rentalFeeAccrued) throws BadRequestException;
    void UpdateBookDetailsOnReturn(int bookReturnId, int userId) throws BadRequestException;
}
