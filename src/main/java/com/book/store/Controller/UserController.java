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

import java.sql.SQLDataException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService usrSrvc;
//    @Autowired
//    private final UserMapper uMap;

//TO DO : get the data from JPARepository. Later on we will discuss about DAO

    @GetMapping("/User")
    public List<User> getUsers(){
            return this.usrSrvc.getUsrList();
    }

    @GetMapping("/User/{ID}")
    public Optional<User> getUsersByID(@PathVariable int ID) throws BadRequestException {
        try{
            return this.usrSrvc.getUsrByID(ID);
        }catch( Exception ex){
            throw  new BadRequestException("Invalid User ID ");
        }

    }

    @PostMapping("/User")
    public ResponseEntity<User> addUser(@RequestBody User usr) throws BadRequestException { // TO DO: take one user at a time
        try{
            return new ResponseEntity<>(this.usrSrvc.addUser(usr),HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @PutMapping("/User/{UserName}") //TO DO : header implementation to check whether  the user is admin or user himself
    public ResponseEntity<String> updateUser(@PathVariable String UserName, @RequestBody User u, @RequestHeader String uName) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            // this.usrSrvc.updateUser(ID, u,userid);// TO DO : update for JPARepository
            return new ResponseEntity<>(this.usrSrvc.updateUser(UserName, u,uName), HttpStatus.OK);
        }catch (Exception ex){
            //return new ResponseEntity<>()
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex);
        }
    }

    @DeleteMapping("/User/{ID}")
    public ResponseEntity<String> deleteUser(@PathVariable int ID,@RequestHeader int userId) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            return new ResponseEntity<>(this.usrSrvc.deleteUser(ID,userId) + " user id has been deleted",HttpStatus.OK);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex);
        }

    }


}
