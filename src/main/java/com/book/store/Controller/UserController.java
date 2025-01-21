package com.book.store.Controller;

import com.book.store.models.domain.User;
//import com.book.store.models.mappers.*;
import com.book.store.service.UserService;
import lombok.*;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;
//    @Autowired
//    private final UserMapper uMap;

//TO DO : get the data from JPARepository. Later on we will discuss about DAO

    @GetMapping("/user")
    public List<User> getUsers(){
            return this.userService.getUsrList();
    }

    @GetMapping("/user/{userName}") // TO DO : Add userid as well
    public User getUsersByUserName(@PathVariable String userName) throws BadRequestException {
        try{
            return this.userService.getUsrByUserName(userName);
        }catch( Exception ex){
            throw  new BadRequestException(ex.getMessage());
        }

    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User usr) throws BadRequestException { // TO DO: take one user at a time
        try{
            return new ResponseEntity<>(this.userService.addUser(usr),HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @PutMapping("/user/{userName}") //TO DO : header implementation to check whether  the user is admin or user himself
    public ResponseEntity<String> updateUser(@PathVariable String userName, @RequestBody User user, @RequestHeader String currentUser) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            //this.userService.updateUser(ID, u,userid);
            return new ResponseEntity<>(this.userService.updateUser(userName, user,currentUser), HttpStatus.OK);
        }catch (Exception ex){
            //return new ResponseEntity<>()
            //System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }
    }

    @DeleteMapping("/user/{userName}")
    public ResponseEntity<String> deleteUser(@PathVariable String userName,@RequestHeader String currentUser) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            return new ResponseEntity<>(this.userService.deleteUser(userName,currentUser),HttpStatus.OK);
        }catch (Exception ex){
            //System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }

    }


}
