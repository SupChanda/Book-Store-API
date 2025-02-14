package com.book.store.service.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.Repository.UserRepository;
import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.BooksPurchaseDao;
import com.book.store.dao.UserDao;
import com.book.store.models.contract.BooksPurchasedRequest;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.BooksPurchased;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.models.mappers.UserMapper;
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
    private int quantity = 0;
    private String transactionType;
    private float purchasedPrice;
    private float rentalFeeAccrued;


    @Autowired
    BooksPurchaseDao booksPurchaseDao;
    @Autowired
    BooksManagementDao booksManagementDao;
    @Autowired
    UserDao userDao;
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BooksMapper booksMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Object getPurchasedBooksDetails() throws BadRequestException {
        try{
            System.out.println("in booksPurchased service impl");
            return booksPurchaseDao.getAllPurchasedBooksDetails();
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        //System.out.println("booksDao.getPurchasedBooksDetails() " + booksDao.getPurchasedBooksDetails());

    }

    public Object getPurchasedBooksDetailsById(int id) throws BadRequestException {
        try{
            System.out.println("in booksPurchased service impl");
            return booksPurchaseDao.getPurchasedBooksDetailsById(id);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        //System.out.println("booksDao.getPurchasedBooksDetails() " + booksDao.getPurchasedBooksDetails());

    }
    @Override
    public String addBookPurchasedOrRentDetails(BooksPurchasedRequest booksPurchasedRequest) throws BadRequestException {
        String bookName;
        String userName;
        try{
            BooksDTO booksDTO = this.booksMapper.toDTO((Books) booksManagementDao.getBooksByIdOrName(booksPurchasedRequest.getId()));
            UserDTO userDTO = this.userMapper.toDTO((BookUser) userDao.getUsrByUserId(booksPurchasedRequest.getCurrentUserId()));
            //Books book = booksRepository.findByTitle(title);
            //BookUser user = userRepository.findByUserName(currentUser);
//            bookId = booksDTO.getId();
//            userId = userDTO.getId();
//            bookName = booksDTO.getTitle();
//            userName = userDTO.getUserName();
            //purchasedPrice = booksDTO.getPrice();
            //rentalFeeAccrued = booksDTO.getRentalFee();
            transactionType = booksPurchasedRequest.getTransactionType();
            quantity = booksPurchasedRequest.getQuantity();


            booksPurchaseDao.addBookPurchasedOrRentDetails(booksDTO,userDTO,transactionType,quantity);

        }
        catch (Exception ex){
            throw new BadRequestException(booksPurchasedRequest.getCurrentUserId() + ex.getMessage());
        }
        return  null ; //userName + " has " + transactionType + " the book '" + bookName + "'";
    }

    @Override
    public String UpdateBookDetailsOnReturn(int bookId, int userId) throws BadRequestException {
        try{
            booksPurchaseDao.UpdateBookDetailsOnReturn(bookId,userId);
        }
        catch (Exception ex){
            return ex.getMessage();
        }
        return "Book Id " + bookId + " is returned";
    }

}
