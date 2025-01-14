package com.book.store.service;

import com.book.store.models.domain.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsrList();
    Optional<User> getUsrByID(int ID);
    User addUser(User usr) throws BadRequestException;
    String updateUser(String UserName, User u, String uName) throws BadRequestException;
    Integer deleteUser(int ID,int userId) throws BadRequestException;
}