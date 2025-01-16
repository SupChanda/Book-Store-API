package com.book.store.service.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.Repository.UserRepository;
import com.book.store.models.domain.Books;
import com.book.store.models.domain.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;

@Service
public class BooksManagementServiceImpl implements BooksManagementService{

    @Autowired
    BooksRepository bkr;
    private static final String err1 = "Admin only and not ";
    private static final String err2 = ", can ";
    private static final String err3 = "this book ";

    @Autowired
    UserRepository usrRepo;
    public BooksManagementServiceImpl(UserRepository usrRepo){
        this.usrRepo = usrRepo;
    }
    @Override
    public List<Books> getBooks(){
        return bkr.findAll();
    }

    @Override
    public String createBooks(Books bk,String admin){
        String bTitle = bk.getTitle();
        User uName = this.usrRepo.findByUserName(admin);
        String errMessage = "The Book : " + bTitle + " is already present.";

        if(uName.getIsAdmin()){
            for(Books b: bkr.findAll()){
                if(b.getTitle().equals(bTitle)){
                    return errMessage;
                }
            }
            bkr.save(bk);
        }
        else{
            System.out.println(err1 + admin + err2 + " create " + err3);
            return  err1 + admin + err2 + " create " + err3;
        }
        return bTitle + " book has been added";
    }
}
