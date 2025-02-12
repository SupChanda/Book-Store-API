package com.book.store.service.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.Repository.UserRepository;
import com.book.store.dao.BooksManagementDao;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.service.BooksManagementService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BooksManagementServiceImpl implements BooksManagementService {

    @Autowired
    BooksRepository booksRepository;
    @Autowired


    private static final String err1 = "Admin only and not ";
    private static final String err2 = ", can ";
    private static final String err3 = "this book ";
    private static final String err4 = "The book: ";
    private static final String err5 = " is not present.";
    private static final String delimiter = "'";

    @Autowired
    UserRepository userRepository;
    @Autowired
    BooksMapper booksMapper;
    @Autowired
    BooksManagementDao booksManagementDao;
    public BooksManagementServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public List<BooksDTO> getBooks() throws BadRequestException {
        List<Books> booksList = (List<Books>) booksManagementDao.getBooksList();
        return this.booksMapper.toDTOList(booksList);
        //return new ArrayList<>();
        //return booksRepository.findAll();
    }

    public Books getBookByTitle(String title) throws BadRequestException {
        if(booksRepository.findByTitle(title) == null){
            throw new BadRequestException("Invalid "+ title);
        }
        return booksRepository.findByTitle(title);
    }

    @Override
    public String createBooks(Books books,String currentUser){
        String bTitle = books.getTitle();
        BookUser uName = this.userRepository.findByUserName(currentUser);
        String errMessage = err4 + bTitle + " is already present.";
        if(uName.getIsAdmin()){
            for(Books b: booksRepository.findAll()){
                if(b.getTitle().equals(bTitle)){
                    return errMessage;
                }
            }
            booksRepository.save(books);
        }
        else{
            System.out.println(err1 + currentUser + err2 + " create " + err3);
            return  err1 + currentUser + err2 + " create " + err3;
        }
        return delimiter + bTitle + delimiter + " book has been added";
    }

    @Transactional
    @Override
    public String updateBooks(String title,Books books,String currentUser){
        String bk_Title = books.getTitle();
        BookUser uName = this.userRepository.findByUserName(currentUser);
        String errMessage = err4 + title + err5;
        System.out.println("bkRepo.findByTitle(title) " + booksRepository.findByTitle(title));
        if(booksRepository.findByTitle(title)== null){
            return errMessage;
        }
        if(uName.getIsAdmin()) {
            for (Books b : booksRepository.findAll()) {
                if (b.getTitle().equalsIgnoreCase(title)) {
                    //System.out.println("b id: " + b.getId() + " b title: " + b.getTitle());
                    books.setId(b.getId());
                    //System.out.println(books);
                    break;
                }
            }
            booksRepository.save(books);
        }
        else{
            System.out.println(err1 + currentUser + err2 + " update " + err3);
            return  err1 + currentUser + err2 + " update " + err3;
        }
        return delimiter + title + delimiter + " book has been updated ";
    }

    @Override
    public String deleteBooks(String title,String currentUser){
        Books bk = booksRepository.findByTitle(title);
        BookUser uName = this.userRepository.findByUserName(currentUser);
        Books bookToBeDeleted = new Books();
        if(bk == null){
            return "The book titled '" + title + "' passed in the url " + err5;
        }else if(uName.getIsAdmin()){
            for(Books b: booksRepository.findAll()){
                if(b.getTitle().equalsIgnoreCase(title)){
                    System.out.println("b : " + b);
                    bookToBeDeleted = b;
                    break;
                }
            }
            booksRepository.delete(bookToBeDeleted);
        }else{
            System.out.println(err1 + currentUser + err2 + " delete " + err3);
            return  err1 + currentUser + err2 + " delete " + err3;
        }
        return delimiter + title + delimiter + " book has been deleted";
    }

}
