package com.book.store.service.impl;

import com.book.store.dao.BooksReviewDao;
import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.BooksReview;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.models.mappers.BooksReviewMapper;
import com.book.store.models.mappers.UserMapper;
import com.book.store.service.BooksManagementService;
import com.book.store.service.BooksPurchaseService;
import com.book.store.service.BooksReviewService;
import com.book.store.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Service
public class BooksReviewServiceImpl implements BooksReviewService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    BooksReviewDao booksReviewDao;
    @Autowired
    BooksReviewMapper booksReviewMapper;
    @Autowired
    BooksMapper booksMapper;
    @Autowired
    BooksManagementService booksManagementService;
    @Autowired
    BooksPurchaseService booksPurchaseService;


    private final Date dateReviewed = Date.valueOf(LocalDate.now());
    @Override
    @Transactional
    public String addBooksReview(BooksReviewRequest booksReviewRequest) throws BadRequestException {
        try{
            //CHECK IF THE PERSON HAVE PURCHASED OR RENTED THE BOOK
            if(booksPurchaseService.getPurchasedBooksDetailsByUserIdAndBookId(booksReviewRequest.getUserId(), booksReviewRequest.getBookId())== null) {
                throw new BadRequestException("Invalid user id: "+ booksReviewRequest.getUserId() + " or book id: " + booksReviewRequest.getBookId());
            }
            Integer bookId = booksReviewRequest.getBookId();
            Books books = booksMapper.toBooksFromDTO((BooksDTO) booksManagementService.getBooksByIdOrName(bookId));

            //System.out.println("Books: " + books);
            Integer userId = booksReviewRequest.getUserId();
            BookUser user = (BookUser) userService.getUsrByUserId(userId);
            //System.out.println("Users: " + user);
            return (String) booksReviewDao.addBookReview(booksReviewRequest,books,user);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public List<BooksReviewRequest> getBooksReview() throws BadRequestException { // return should be BookReview class
        try{
            return booksReviewMapper.toBooksReviewRequestListFromRequest(booksReviewDao.getBooksReview());
            //return booksReviewDao.getBooksReview(); //booksReviewRequest; //booksReviewDao.getBooksReview();
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public List<BooksReview> getBooksReviewByBookId(int bookId) throws BadRequestException {
        try{
            return booksReviewDao.getBooksReviewByBookId(bookId);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}
