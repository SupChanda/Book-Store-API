package com.book.store.dao;

import com.book.store.Repository.UserRepository;
import com.book.store.models.contract.UserRequest;
import com.book.store.models.dto.UserDTO;
import org.apache.coyote.BadRequestException;

public interface UserDao {

    boolean isUserAdmin(String currentUser) throws BadRequestException;
    boolean addUser(String uName, UserRequest userRequest);
    void updateUser(Integer userId, UserRequest userRequest) throws BadRequestException;
    Object getUsrByUserName(String userName) throws BadRequestException;
    Object getUsrByUserId(Integer userId) throws BadRequestException;
    void deleteUser(Integer userId, String currentUser) throws BadRequestException;
}
