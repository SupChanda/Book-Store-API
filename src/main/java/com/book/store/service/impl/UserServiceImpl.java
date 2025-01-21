package com.book.store.service.impl;

import com.book.store.models.domain.User;
import com.book.store.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.book.store.Repository.UserRepository;

import javax.management.BadAttributeValueExpException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    User user;
    @Autowired
    List<User> uList;
    @Autowired
    UserRepository usrRepo;

    Boolean nameAlreadyPresent = false;

    public List<User> getUsrList() {
            return usrRepo.findAll();
    }
    @Override
    public User getUsrByUserName(String userName) throws BadRequestException {
            User user =  usrRepo.findByUserName(userName);
            if(user == null){
                throw new BadRequestException("Invalid username");
            }
            return user;
    }
    @Override
    public String addUser(User user) throws BadRequestException {
        String uName = user.getUserName();
        String errMessage = "The username " + uName + " is already present.";
        for(User u: usrRepo.findAll()){
            if (u.getUserName().equalsIgnoreCase(uName)){
                //nameAlreadyPresent = true;
                return errMessage;
               // throw new BadRequestException(errMessage);
            }
        }
        //if(!nameAlreadyPresent){
            //System.out.println("name already present : " + nameAlreadyPresent);
            usrRepo.save(user);
//        }else{
//            nameAlreadyPresent = false;
//            System.out.println(errMessage);
//            throw new BadRequestException(errMessage);
//        }
        return uName + " user record has been added";
    }

    @Override
    public String updateUser(String userName, User user, String currentUser) throws BadRequestException {
        Boolean isAdmin = false;
        Boolean err = false;
        String errMessage = currentUser + " cannot update " + userName +"'s record because either "
                + currentUser + " is not an admin user or " + userName + "'s record doesn't exists";
        User adminUser = usrRepo.findByUserName(currentUser);
        User usr = usrRepo.findByUserName(userName);
        if(adminUser == null|| usr == null){
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        if(adminUser.getIsAdmin() || Objects.equals(userName, currentUser)){
                user.setId(usr.getId());
                usrRepo.save(user);
        }
        else{
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        return currentUser + " updated " + userName + " record";
    }

    @Override
    public String deleteUser(String userName, String currentUser) throws BadRequestException {
        String errMessage =  currentUser + " cannot delete " + userName +"'s record because either "
                + currentUser + " is not an admin user or " + userName + "'s record doesn't exists";
        User adminUser = usrRepo.findByUserName(currentUser);
        User usr = usrRepo.findByUserName(userName);
        if(usrRepo.findByUserName(currentUser) == null || usrRepo.findByUserName(userName) == null ){
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        else if(adminUser.getIsAdmin() || userName.equalsIgnoreCase(currentUser)){
            this.usrRepo.deleteById(usr.getId());
        }else{
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        return currentUser + " deleted " + userName + "'s record";
    }
}
