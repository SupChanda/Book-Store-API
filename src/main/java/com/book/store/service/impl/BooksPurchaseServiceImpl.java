package com.book.store.service.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.Repository.UserRepository;
import com.book.store.dao.BooksDao;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.Books_Purchased;
import com.book.store.models.domain.User;
import com.book.store.service.BooksPurchaseService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BooksPurchaseServiceImpl implements BooksPurchaseService {



    private int bookId;
    private int userId;
    private int quantity;
    private float purchasedPrice;
    private float rentalFeeAccrued;


    @Autowired
    BooksDao booksDao;
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public List<Books_Purchased> getPurchasedBooksDetails() throws BadRequestException {
        try{
            return booksDao.getAllPurchasedBooksDetails();
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        //System.out.println("booksDao.getPurchasedBooksDetails() " + booksDao.getPurchasedBooksDetails());

    }
    @Override
    public String addBookPurchasedOrRentDetails(String title,String transactionType,int quantity,String currentUser) throws BadRequestException {
        try{
            Books book = booksRepository.findByTitle(title);
            User user = userRepository.findByUserName(currentUser);
            bookId = book.getId();
            userId = user.getId();
            purchasedPrice = book.getPrice();
            rentalFeeAccrued = book.getRentalFee();

            booksDao.addBookPurchasedOrRentDetails(bookId,userId,transactionType,quantity,purchasedPrice,rentalFeeAccrued);

        }
        catch (Exception ex){
            throw new BadRequestException(currentUser + ex.getMessage());
        }
        return  currentUser + " has " + transactionType + " the book '" + title + "'";
    }

    @Override
    public String UpdateBookDetailsOnReturn(int bookId, int userId) throws BadRequestException {
        try{
            booksDao.UpdateBookDetailsOnReturn(bookId,userId);
        }
        catch (Exception ex){
            return ex.getMessage();
        }
        return "Book Id " + bookId + " is returned";
    }

}
