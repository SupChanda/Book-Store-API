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
import java.util.Locale;

@Service
public class BooksManagementServiceImpl implements BooksManagementService{

    @Autowired
    BooksRepository bkRepo;
    private static final String err1 = "Admin only and not ";
    private static final String err2 = ", can ";
    private static final String err3 = "this book ";
    private static final String err4 = "The book ";
    private static final String err5 = " is not present.";
    private static final String delimiter = "'";

    @Autowired
    UserRepository usrRepo;
    public BooksManagementServiceImpl(UserRepository usrRepo){
        this.usrRepo = usrRepo;
    }
    @Override
    public List<Books> getBooks(){
        return bkRepo.findAll();
    }

    public Books getBookByTitle(String title) throws BadRequestException {
        if(bkRepo.findByTitle(title) == null){
            throw new BadRequestException("Invalid "+ title);
        }
        return bkRepo.findByTitle(title);
    }

    @Override
    public String createBooks(Books bk,String admin){
        String bTitle = bk.getTitle();
        User uName = this.usrRepo.findByUserName(admin);
        String errMessage = err4 + bTitle + " is already present.";

        if(uName.getIsAdmin()){
            for(Books b: bkRepo.findAll()){
                if(b.getTitle().equals(bTitle)){
                    return errMessage;
                }
            }
            bkRepo.save(bk);
        }
        else{
            System.out.println(err1 + admin + err2 + " create " + err3);
            return  err1 + admin + err2 + " create " + err3;
        }
        return delimiter + bTitle + delimiter + " book has been added";
    }

    @Override
    public String updateBooks(String title,Books bk,String admin){
        String bk_Title = bk.getTitle();
        User uName = this.usrRepo.findByUserName(admin);
        String errMessage = err4 + bk_Title + err5;
        System.out.println("bkRepo.findByTitle(title) " + bkRepo.findByTitle(title));
        if(bkRepo.findByTitle(title)== null){
            return "The book titled '" + title + "' passed in the url " + err5;
        }
        if(uName.getIsAdmin()){
            for(Books b: bkRepo.findAll()){
                if(b.getTitle().equalsIgnoreCase(title)){
                    //System.out.println("b id: " + b.getId() + " b title: " + b.getTitle());
                    bk.setId(b.getId());
                    //System.out.println(bk);
                    bkRepo.save(bk);
                    break;
                }
            }
        }
        else{
            System.out.println(err1 + admin + err2 + " update " + err3);
            return  err1 + admin + err2 + " update " + err3;
        }
        return delimiter + title + delimiter + " book has been updated to "+ delimiter + bk_Title + delimiter;
    }

    @Override
    public String deleteBooks(String title,String admin){
        Books bk = bkRepo.findByTitle(title);
        User uName = this.usrRepo.findByUserName(admin);
        Books tempBk = new Books();
        if(bk == null){
            return "The book titled '" + title + "' passed in the url " + err5;
        }else if(uName.getIsAdmin()){
            for(Books b: bkRepo.findAll()){
                if(b.getTitle().equalsIgnoreCase(title)){
                    //System.out.println("b id: " + b.getId() + " b title: " + b.getTitle());
                    tempBk = b;
                    break;
                }
            }
        }else{
            System.out.println(err1 + admin + err2 + " delete " + err3);
            return  err1 + admin + err2 + " delete " + err3;
        }
        bkRepo.delete(tempBk);
        return delimiter + title + delimiter + " book has been deleted";
    }
}
