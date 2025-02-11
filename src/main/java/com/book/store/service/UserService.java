package com.book.store.service;

import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
//import com.book.store.models.domain.User;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<BookUser> getUsrList();
    Object getUsrByUserName(String userName) throws BadRequestException;
    Object getUsrByUserId(Integer userId) throws BadRequestException;
    String addUser(UserRequest userRequest) throws BadRequestException;
    String updateUser(UserRequest userRequest, String currentUser) throws BadRequestException;
    String deleteUser(int userId,String currentUser) throws BadRequestException;
}