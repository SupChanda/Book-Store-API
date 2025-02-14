package com.book.store.service.impl;

import com.book.store.dao.UserDao;
import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.UserMapper;
import com.book.store.service.UserService;
import org.apache.catalina.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.book.store.Repository.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    BookUser user;
    @Autowired
    List<BookUser> uList;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDao userDao;

    Boolean nameAlreadyPresent = false;

    public List<BookUser> getUsrList() {
            return userRepository.findAll();
    }
    @Override
    public Object getUsrByUserName(String userName) throws BadRequestException {
        try {
            return userDao.getUsrByUserName(userName);
        } catch (Exception ex) {
            throw new BadRequestException("Invalid Username: " + userName);
        }
    }
    @Override
    public Object getUsrByUserId(Integer userId) throws BadRequestException {
        try {
            return userDao.getUsrByUserId(userId);
        } catch (Exception ex) {
            throw new BadRequestException("Invalid UserId: " + userId);
        }
    }
    @Override
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
    public String updateUser(UserRequest userRequest, String currentUser) throws BadRequestException {
        try {

            String userName = userRequest.getUserName();
            int userId = userRequest.getId();
            boolean isAdminUser = userDao.isUserAdmin(currentUser);
            if (!isAdminUser) {
                throw new BadRequestException("Invalid Admin user: " + currentUser + "!");
            }

            UserDTO adminUserFound = userMapper.toDTO((BookUser) userDao.getUsrByUserName(currentUser));
            UserDTO userNameFound = userMapper.toDTO((BookUser) userDao.getUsrByUserId(userId));

            if (isAdminUser || userNameFound.getId().equals(adminUserFound.getId())) {
                System.out.println("Is current user admin? : " + adminUserFound.getIsAdmin());
                userDao.updateUser(userId, userRequest);
                return currentUser + " updated " + userName + " record";
            }else {
                throw new BadRequestException("Invalid userid: "+ userId );
            }
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }

    }

    @Override
    public String deleteUser(int userId, String currentUser) throws BadRequestException {
        try {
            if (userDao.isUserAdmin(currentUser)) {
                throw new BadRequestException(currentUser + " is not an admin user!");
            }
            Integer userIdFound = userMapper.toDTO((BookUser) userDao.getUsrByUserId(userId)).getId();
            UserDTO isAdminUser = userMapper.toDTO((BookUser) userDao.getUsrByUserName(currentUser));
            if(isAdminUser.getId().equals(userIdFound)){
                userDao.deleteUser(userId, currentUser);
                return "User ID : " + userId + " has been deleted";
            }
//            else if (userIdFound == null) {
//                throw new BadRequestException("Invalid userId: " + userId);
//            }
            else {
                throw new BadRequestException(currentUser + " ,you cannot delete userId: " + userId);
            }
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}
