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
    public User addUser(User usr) throws BadRequestException {
        String uName = usr.getUserName();
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
            System.out.println("The username " + uName + " is already present.");
            throw new BadRequestException("The username " + uName + " is already present.");
        }
        return usr;
    }

    @Override
    public String updateUser(String UserName, User u, String uName) throws BadRequestException {
        Boolean isAdmin = false;
        Boolean err = false;
        User adminUser = usrRepo.findByUserName(uName);
        User usr = usrRepo.findByUserName(UserName);
        if(adminUser.getIsAdmin() || Objects.equals(UserName, uName)){
                u.setId(usr.getId());
                usrRepo.save(u);
        }
        else{
            throw new BadRequestException("UserName : = " + uName + " is not an admin user name");
        }
        return UserName + " details have been updated ";
    }

    @Override
    public String deleteUser(String UserName, String uName) throws BadRequestException {
        Boolean IsAdmin = usrRepo.findByUserName(uName).getIsAdmin();
        Integer ID = usrRepo.findByUserName(UserName).getId();
        System.out.println("Is admin: " + IsAdmin + " ID: " + ID);
        if(IsAdmin || UserName.equals(uName)){
            this.usrRepo.deleteById(ID);
        }else{
            throw new BadRequestException("The user name " + UserName + " is neither an admin nor the user whose id is : " + ID);
        }
        return UserName;
    }
}
