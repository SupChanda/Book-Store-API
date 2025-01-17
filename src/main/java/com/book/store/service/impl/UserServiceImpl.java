package com.book.store.service.impl;

import com.book.store.models.domain.User;
import com.book.store.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.book.store.Repository.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    User usr;
    @Autowired
    List<User> uList;
    @Autowired
    UserRepository usrRepo;

    Boolean nameAlreadyPresent = false;

    public List<User> getUsrList() {
            return usrRepo.findAll();
    }
    @Override
    public User getUsrByUserName(String userName) {
            return usrRepo.findByUserName(userName);
    }
    @Override
    public String addUser(User usr) throws BadRequestException {
        String uName = usr.getUserName();
        String errMessage = "The username " + uName + " is already present.";
        for(User u: usrRepo.findAll()){
            if (u.getUserName().equals(uName)){
                nameAlreadyPresent = true;
                break;
            }
        }
        if(!nameAlreadyPresent){
            //System.out.println("name already present : " + nameAlreadyPresent);
            usrRepo.save(usr);
        }else{
            nameAlreadyPresent = false;
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        return uName + " user record has been added";
    }

    @Override
    public String updateUser(String UserName, User u, String uName) throws BadRequestException {
        Boolean isAdmin = false;
        Boolean err = false;
        String errMessage = uName + " cannot update " + UserName +"'s record because either "
                + uName + " is not an admin user or " + UserName + "'s record doesn't exists";
        User adminUser = usrRepo.findByUserName(uName);
        User usr = usrRepo.findByUserName(UserName);
        if(adminUser == null|| usr == null){
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        if(adminUser.getIsAdmin() || Objects.equals(UserName, uName)){
                u.setId(usr.getId());
                usrRepo.save(u);
        }
        else{
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        return uName + " updated " + UserName + " record";
    }

    @Override
    public String deleteUser(String UserName, String uName) throws BadRequestException {
        String errMessage =  uName + " cannot delete " + UserName +"'s record because either "
                + uName + " is not an admin user or " + UserName + "'s record doesn't exists";
        User adminUser = usrRepo.findByUserName(uName);
        User usr = usrRepo.findByUserName(UserName);
        if(usrRepo.findByUserName(uName) == null || usrRepo.findByUserName(UserName) == null ){
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        else if(adminUser.getIsAdmin() || UserName.equalsIgnoreCase(uName)){
            this.usrRepo.deleteById(usr.getId());
        }else{
            System.out.println(errMessage);
            throw new BadRequestException(errMessage);
        }
        return uName + " deleted " + UserName + "'s record";
    }
}
