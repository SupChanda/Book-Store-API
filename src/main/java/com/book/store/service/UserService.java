package com.book.store.service;

import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.UserDTO;
import org.apache.coyote.BadRequestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<BookUser> getUsrList();
    BookUser getUsrByUserName(String userName) throws BadRequestException;
    BookUser getUsrByUserId(Integer userId) throws BadRequestException;
    String addUser(UserRequest userRequest) throws BadRequestException;
    String updateUser(UserRequest userRequest, String currentUser) throws BadRequestException;
    String deleteUser(int userId,String currentUser) throws BadRequestException;
}