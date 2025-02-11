package com.book.store.Controller;

import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
//import com.book.store.models.domain.User;
//import com.book.store.models.mappers.*;
//import com.book.store.models.dto.UserDTO;
//import com.book.store.models.mappers.UserMapper;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.UserMapper;
import com.book.store.service.UserService;
import jakarta.validation.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Log4j2
@RequiredArgsConstructor
public class UserController {
    //@Autowired
    private final UserService userService;

    //@Autowired
    private final UserMapper userMapper;


//TO DO : get the data from JPARepository. Later on we will discuss about DAO

    @GetMapping("/user")
    public List<UserDTO> getUsers(){
        List<BookUser> user = this.userService.getUsrList();
        List<UserDTO> userDTO = this.userMapper.userToDTOList(user);
        return userDTO;
    }

    @GetMapping("/user/name/{userName}") // TO DO : Add userid as well
    public ResponseEntity<Object> getUsersByUserName(@PathVariable String userName) throws BadRequestException {
        try{
            UserDTO userDTO = userMapper.toDTO((BookUser) this.userService.getUsrByUserName(userName));
            //System.out.println("in controller try");
            return new ResponseEntity<>(userDTO,HttpStatus.OK);
            //return this.userService.getUsrByUserName(userName);
        }catch(Exception ex){
            //System.out.println("catch Controller:" + ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/user/id/{userId}")
    public ResponseEntity<Object> getUsersByUserID(@PathVariable Integer userId) throws BadRequestException {
        try{
            UserDTO userDTO = userMapper.toDTO((BookUser) this.userService.getUsrByUserId(userId));
            //System.out.println("in controller try");
            return new ResponseEntity<>(userDTO,HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDTO userDTO) throws BadRequestException { // TO DO: take one user at a time
        try{
            UserRequest userRequest = userMapper.toUserRequest(userDTO);
            return new ResponseEntity<>(this.userService.addUser(userRequest),HttpStatus.OK);
        }catch(Exception ex){
            //System.out.println("yes");
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO, @RequestHeader String currentUser) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            UserRequest userRequest = userMapper.toUserRequest(userDTO);
            //this.userService.updateUser(ID, u,userid);
            return new ResponseEntity<>(this.userService.updateUser(userRequest,currentUser), HttpStatus.OK);
        }catch (Exception ex){
            //return new ResponseEntity<>()
            //System.out.println(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId,@RequestHeader String currentUser) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            return new ResponseEntity<>(this.userService.deleteUser(userId,currentUser),HttpStatus.OK);
        }catch (Exception ex){
            //System.out.println(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }


}



//    @PutMapping("/user/{userName}") //TO DO : header implementation to check whether  the user is admin or user himself
//    public ResponseEntity<String> updateUser(@PathVariable String userName, @RequestBody UserDTO userDTO, @RequestHeader String currentUser) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
//        try{
//            UserRequest userRequest = userMapper.toUserRequest(userDTO);
//            //this.userService.updateUser(ID, u,userid);
//            return new ResponseEntity<>(this.userService.updateUser(userName, userRequest,currentUser), HttpStatus.OK);
//        }catch (Exception ex){
//            //return new ResponseEntity<>()
//            //System.out.println(ex.getMessage());
//            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }