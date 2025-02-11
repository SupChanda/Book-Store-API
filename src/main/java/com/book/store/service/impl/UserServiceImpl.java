package com.book.store.service.impl;

import com.book.store.dao.UserDao;
import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.UserMapper;
import com.book.store.service.UserService;
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
            //System.out.println("yes back in service catch: " + ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }
    }
    @Override
    public Object getUsrByUserId(Integer userId) throws BadRequestException {
        try {
            System.out.println("in service implementation");
            return userDao.getUsrByUserId(userId);
        } catch (Exception ex) {
            //System.out.println("yes back in service catch: " + ex.getMessage());
            throw new BadRequestException(ex.getMessage());
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
//        for(User u: userRepository.findAll()){
//            if (u.getUserName().equalsIgnoreCase(userName)){
//                //nameAlreadyPresent = true;
//                return errMessage;
//               // throw new BadRequestException(errMessage);
//            }
//        }
        //if(!nameAlreadyPresent){
            //System.out.println("name already present : " + nameAlreadyPresent);
            //userRepository.save(user);
//        }else{
//            nameAlreadyPresent = false;
//            System.out.println(errMessage);
//            throw new BadRequestException(errMessage);
//        }
        //return userName + " user record has been added";
    }

    @Override
    public String updateUser(UserRequest userRequest, String currentUser) throws BadRequestException {
        try {
            Boolean isAdmin = false;
            Boolean err = false;
            String userName = userRequest.getUserName();
            int userId = userRequest.getId();

            UserDTO adminUserFound = userMapper.toDTO((BookUser) userDao.getUsrByUserName(currentUser));
            UserDTO userNameFound = userMapper.toDTO((BookUser) userDao.getUsrByUserId(userId));


            if (adminUserFound == null){
                throw new BadRequestException("Invalid Admin user: "+ currentUser + "!" );
            }else if (userNameFound.getId().equals(adminUserFound.getId()) || adminUserFound.getIsAdmin()) {
                System.out.println("Is current user admin? : " + adminUserFound.getIsAdmin());
                userDao.updateUser(userId, userRequest);
                return currentUser + " updated " + userName + " record";
            }else {
                throw new BadRequestException("Invalid userid: "+ userId + "!" );
            }
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }

    }

    @Override
    public String deleteUser(int userId, String currentUser) throws BadRequestException {
        try {
            Integer userIdFound = userMapper.toDTO((BookUser) userDao.getUsrByUserId(userId)).getId();
            UserDTO isAdminUser = userMapper.toDTO((BookUser) userDao.getUsrByUserName(currentUser));
            if(isAdminUser.getId().equals(userIdFound)){
                userDao.deleteUser(userId, currentUser);
                return "User ID : " + userId + " has been deleted";
            }
            else if (userIdFound == null) {
                throw new BadRequestException("Invalid userId: " + userId);
            } else if (!isAdminUser.getIsAdmin()) {
                throw new BadRequestException(currentUser + " is not an admin user!");
            }else {
                throw new BadRequestException(currentUser + " ,you cannot delete userId: " + userId);
            }
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }

//        String errMessage =  currentUser + " cannot delete " + userName +"'s record because either "
//                + currentUser + " is not an admin user or " + userName + "'s record doesn't exists";
//        BookUser adminUser = userRepository.findByUserName(currentUser);
//        BookUser usr = userRepository.findByUserName(userName);
//        if(userRepository.findByUserName(currentUser) == null || userRepository.findByUserName(userName) == null ){
//            System.out.println(errMessage);
//            throw new BadRequestException(errMessage);
//        }
//        else if(userName.equalsIgnoreCase(currentUser) || userRepository.findByUserName(currentUser).getIsAdmin()){
//            this.userRepository.deleteById(usr.getId());
//        }else{
//            System.out.println(errMessage);
//            throw new BadRequestException(errMessage);
//        }
//        return currentUser + " deleted " + userName + "'s record";
    }
}
