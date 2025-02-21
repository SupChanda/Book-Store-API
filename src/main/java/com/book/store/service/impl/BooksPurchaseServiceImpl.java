package com.book.store.service.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.Repository.UserRepository;
import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.BooksPurchaseDao;
import com.book.store.dao.UserDao;
import com.book.store.dao.impl.GenericDaoImpl;
import com.book.store.models.contract.BooksPurchasedRequest;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.BooksPurchased;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.BooksPurchasedDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.models.mappers.UserMapper;
import com.book.store.service.BooksPurchaseService;
import jakarta.persistence.NoResultException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BooksPurchaseServiceImpl extends GenericDaoImpl<BooksPurchaseDao> implements BooksPurchaseService {



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
    public Object getPurchasedBooksDetailsByUserIdAndBookId(int userId,int bookId) throws BadRequestException {
        try{
            System.out.println("in booksPurchased user and book Id service impl");
            return booksPurchaseDao.getPurchasedBooksDetailsByUserIdAndBookId(userId,bookId);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
    @Override
    @Transactional
    public String addBookPurchasedOrRentDetails(BooksPurchasedRequest booksPurchasedRequest) throws BadRequestException {
        String bookName;
        String userName;
        try{
            BooksDTO booksDTO;
            UserDTO userDTO;

            try{
                booksDTO = this.booksMapper.toDTO((Books) booksManagementDao.getBooksByIdOrName(booksPurchasedRequest.getBookId()));
                bookName = booksDTO.getTitle();
            }catch(NoResultException ex){
                return "Invalid Book Id: " + booksPurchasedRequest.getBookId();
            }
            try{
                userDTO = this.userMapper.toDTO((BookUser) userDao.getUsrByUserId(booksPurchasedRequest.getCurrentUserId()));
                userName = userDTO.getUserName();
            }catch(NoResultException ex){
                return "Invalid User Id: " + booksPurchasedRequest.getCurrentUserId();
            }
            transactionType = booksPurchasedRequest.getTransactionType();
            quantity = booksPurchasedRequest.getQuantity();


            booksPurchaseDao.addBookPurchasedOrRentDetails(booksDTO,userDTO,transactionType,quantity);

        }
        catch (Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        return  userName + " has " + transactionType.toLowerCase() + " the book '" + bookName + "'";
    }

    @Override
    @Transactional
    public String UpdateBookDetailsOnReturn(BooksPurchasedDTO booksPurchasedDTO) throws BadRequestException {
        String bookName;
        String userName;
        try {
            BooksDTO booksDTO;
            UserDTO userDTO;

            try {
                booksDTO = this.booksMapper.toDTO((Books) booksManagementDao.getBooksByIdOrName(booksPurchasedDTO.getBookId()));
                bookName = booksDTO.getTitle();
            } catch (NoResultException ex) {
                return "Invalid Book Id: " + booksPurchasedDTO.getBookId();
            }
            try {
                userDTO = this.userMapper.toDTO((BookUser) userDao.getUsrByUserId(booksPurchasedDTO.getUserId()));
                userName = userDTO.getUserName();
            } catch (NoResultException ex) {
                return "Invalid User Id: " + booksPurchasedDTO.getUserId();
            }
            booksPurchaseDao.UpdateBookDetailsOnReturn(booksPurchasedDTO);

//        try{
//
        }
        catch (Exception ex){
            return ex.getMessage();
        }
        return "Book Id: " + booksPurchasedDTO.getBookId() + " has been returned"; //+ bookId + " is returned";
        }

}
