package com.book.store.service.impl;

import com.book.store.dao.UserDao;
import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.UserMapper;
import com.book.store.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.book.store.Repository.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDao userDao;
    public List<BookUser> getUsrList() {
            return userRepository.findAll();
    }
    @Override
    public BookUser getUsrByUserName(String userName) throws BadRequestException {
        try {

            return (BookUser)userDao.getUsrByUserName(userName);
        } catch (Exception ex) {
            System.out.println("getUsrByUserId service impl");
            throw new BadRequestException("Invalid Username: " + userName);
        }
    }
    @Override
    public BookUser getUsrByUserId(Integer userId) throws BadRequestException {
        try {
            return (BookUser)userDao.getUsrByUserId(userId);
        } catch (Exception ex) {
            throw new BadRequestException("Invalid UserId: " + userId);
        }
    }
    @Override
    @Transactional
    public String addUser(UserRequest userRequest) throws BadRequestException {
        try{
            String userName = userRequest.getUserName();
            boolean userAdded = userDao.addUser(userName, userRequest);
            if(userAdded){
                return userName + " user record has been added";
            }
            return "The username " + userName + " is already present.";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }

    @Override
    @Transactional
    public String updateUser(UserRequest userRequest, String currentUser) throws BadRequestException {
        try {
            String userName = userRequest.getUserName();
            int userId = userRequest.getId();
            boolean isAdminUser = userDao.isUserAdmin(currentUser);
            UserDTO userNameFound = userMapper.toDTO((BookUser) userDao.getUsrByUserId(userId));

            if (isAdminUser || userRequest.getUserName().equalsIgnoreCase(currentUser)) {
                userDao.updateUser(userId, userRequest);
                return currentUser + " updated " + userName + " record";
            }else {
                throw new BadRequestException("Invalid userid: "+ userRequest.getId() );
            }
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }

    }

    @Override
    @Transactional
    public String deleteUser(int userId, String currentUser) throws BadRequestException {
        try {
            boolean userIdMatch=false;
            Integer userIdFound = userMapper.toDTO((BookUser) userDao.getUsrByUserId(userId)).getId();
            UserDTO isAdminUser = userMapper.toDTO((BookUser) userDao.getUsrByUserName(currentUser));

            if(isAdminUser.getId().equals(userIdFound)|| userDao.isUserAdmin(currentUser) ){
                userIdMatch = true;
            }

            if (!userIdMatch) {
                throw new BadRequestException(currentUser + " is either not an admin user neither has userid: " + userId);
            }else { //
                userDao.deleteUser(userId, currentUser);
                return "User ID : " + userId + " has been deleted";
            }
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}
