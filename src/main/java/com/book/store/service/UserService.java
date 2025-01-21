package com.book.store.service;

import com.book.store.models.domain.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsrList();
    User getUsrByUserName(String userName) throws BadRequestException;
    String addUser(User usr) throws BadRequestException;
    String updateUser(String UserName, User u, String currentUser) throws BadRequestException;
    String deleteUser(String UserName,String currentUser) throws BadRequestException;
}