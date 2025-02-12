package com.book.store.dao;

import com.book.store.models.contract.UserRequest;
import org.apache.coyote.BadRequestException;

public interface BooksManagementDao {

    Object getBooksList() throws BadRequestException;
//    boolean addBooks(String uName, UserRequest userRequest);
//    void updateUser(Integer userId, UserRequest userRequest) throws BadRequestException;
//    Object getUsrByUserName(String userName) throws BadRequestException;
//    Object getUsrByUserId(Integer userId) throws BadRequestException;
//
//    void deleteUser(Integer userId, String currentUser) throws BadRequestException;
}
