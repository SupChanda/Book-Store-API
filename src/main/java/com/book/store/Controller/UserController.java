package com.book.store.Controller;

import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.UserMapper;
import com.book.store.service.UserService;
import jakarta.validation.*;
import lombok.*;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    //@Autowired
    private final UserService userService;

    //@Autowired
    private final UserMapper userMapper;

    @GetMapping("/user")
    public List<UserDTO> getUsers(){
        List<BookUser> user = this.userService.getUsrList();
        List<UserDTO> userDTO = this.userMapper.userToDTOList(user);
        return userDTO;
    }
    @GetMapping("/user/name/{userName}")
    public ResponseEntity<Object> getUsersByUserName(@PathVariable String userName) throws BadRequestException {
        try{
            BookUser bookUser = this.userService.getUsrByUserName(userName);
            return new ResponseEntity<>(bookUser,HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/user/id/{userId}")
    public ResponseEntity<Object> getUsersByUserID(@PathVariable Integer userId) throws BadRequestException {
        try{
            BookUser bookUser = this.userService.getUsrByUserId(userId);
            return new ResponseEntity<>(bookUser,HttpStatus.OK);
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
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO, @RequestHeader String currentUser) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            UserRequest userRequest = userMapper.toUserRequest(userDTO);
            return new ResponseEntity<>(this.userService.updateUser(userRequest,currentUser), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId,@RequestHeader String currentUser) throws BadRequestException { // TO DO : Validation if the user exists , else throw an exception
        try{
            return new ResponseEntity<>(this.userService.deleteUser(userId,currentUser),HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
