package com.book.store.dao;

import com.book.store.models.contract.UserRequest;
import com.book.store.models.dto.BooksDTO;
import org.apache.coyote.BadRequestException;

public interface BooksManagementDao {

    Object getBooksList() throws BadRequestException;
    Object getBooksById(Object obj) throws BadRequestException;
    Object getBooksByIdOrName(Object obj) throws BadRequestException;

    boolean createBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException;
    String updateBooks(BooksDTO booksDTO,String currentUser) throws BadRequestException;
    String deleteBooks(int id,String currentUser) throws BadRequestException;
//    void updateUser(Integer userId, UserRequest userRequest) throws BadRequestException;
//    Object getUsrByUserName(String userName) throws BadRequestException;
//    Object getUsrByUserId(Integer userId) throws BadRequestException;
//
//    void deleteUser(Integer userId, String currentUser) throws BadRequestException;
}
