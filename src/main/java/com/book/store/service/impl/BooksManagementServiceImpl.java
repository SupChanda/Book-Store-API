package com.book.store.service.impl;

import com.book.store.Repository.BooksRepository;
import com.book.store.Repository.UserRepository;
import com.book.store.dao.BooksManagementDao;
import com.book.store.models.contract.BooksRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.service.BooksManagementService;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;

@Service
public class BooksManagementServiceImpl implements BooksManagementService {

//    @Autowired
//    BooksRepository booksRepository;

//    @Autowired
//    UserRepository userRepository;
    @Autowired
    BooksMapper booksMapper;
    @Autowired
    BooksManagementDao booksManagementDao;

//    public BooksManagementServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public List<BooksDTO> getBooks() throws BadRequestException {
        List<Books> booksList = (List<Books>) booksManagementDao.getBooksList();
        return this.booksMapper.toDTOList(booksList);

    }


    public Object getBooksByIdOrName(Object obj) throws BadRequestException {
        try {
            //System.out.println("Yes in service impl");
            //System.out.println("this.booksMapper.toDTO((Books) booksManagementDao.getBooksById(id)): " + this.booksMapper.toDTO((Books) booksManagementDao.getBooksById(id)));
            //System.out.println("booksDTO: " + booksDTO);
            return this.booksMapper.toDTO((Books) booksManagementDao.getBooksByIdOrName(obj));
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public String createBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException {
        try {
            if (booksManagementDao.createBooks(booksDTO, currentUser)) {
                return booksDTO.getTitle() + " has been added";
            }
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return booksDTO.getTitle() + " has been added";
    }

    @Override
    public String updateBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException {
        String result;
        try {
            result = booksManagementDao.updateBooks(booksDTO, currentUser);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return result;
    }

    @Override
    public String deleteBooks(BooksRequest booksRequest, String currentUser) throws BadRequestException {
        String result;
        int BookID = booksRequest.getId();
        try {

            result = booksManagementDao.deleteBooks(BookID, currentUser);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return result;
    }
}

//    @Override
//    public String deleteBooks(String title,String currentUser){
//        Books bk = booksRepository.findByTitle(title);
//        BookUser uName = this.userRepository.findByUserName(currentUser);
//        Books bookToBeDeleted = new Books();
//        if(bk == null){
//            return "The book titled '" + title + "' passed in the url " + err5;
//        }else if(uName.getIsAdmin()){
//            for(Books b: booksRepository.findAll()){
//                if(b.getTitle().equalsIgnoreCase(title)){
//                    System.out.println("b : " + b);
//                    bookToBeDeleted = b;
//                    break;
//                }
//            }
//            booksRepository.delete(bookToBeDeleted);
//        }else{
//            System.out.println(err1 + currentUser + err2 + " delete " + err3);
//            return  err1 + currentUser + err2 + " delete " + err3;
//        }
//        return delimiter + title + delimiter + " book has been deleted";
//    }
//
//}


//        String errMessage = err4 + bTitle + " is already present.";
//        String bTitle = books.getTitle();
//        BookUser uName = this.userRepository.findByUserName(currentUser);
//
//        if(uName.getIsAdmin()){
//            for(Books b: booksRepository.findAll()){
//                if(b.getTitle().equals(bTitle)){
//                    return errMessage;
//                }
//            }
//            booksRepository.save(books);
//        }
//        else{
//            System.out.println(err1 + currentUser + err2 + " create " + err3);
//            return  err1 + currentUser + err2 + " create " + err3;
//        }
//        return delimiter + bTitle + delimiter + " book has been added";

//
//
//String bk_Title = books.getTitle();
//BookUser uName = this.userRepository.findByUserName(currentUser);
//String errMessage = err4 + title + err5;
//        System.out.println("bkRepo.findByTitle(title) " + booksRepository.findByTitle(title));
//        if(booksRepository.findByTitle(title)== null){
//        return errMessage;
//        }
//                if(uName.getIsAdmin()) {
//        for (Books b : booksRepository.findAll()) {
//        if (b.getTitle().equalsIgnoreCase(title)) {
//        //System.out.println("b id: " + b.getId() + " b title: " + b.getTitle());
//        books.setId(b.getId());
//        //System.out.println(books);
//        break;
//        }
//        }
//        booksRepository.save(books);
//        }
//                else{
//                System.out.println(err1 + currentUser + err2 + " update " + err3);
//            return  err1 + currentUser + err2 + " update " + err3;
//        }
//                return delimiter + title + delimiter + " book has been updated ";