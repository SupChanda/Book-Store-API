package com.book.store.Controller;

import com.book.store.models.contract.BooksRequest;
import com.book.store.models.domain.Books;
//import com.book.store.models.domain.Books_Purchased;
import com.book.store.models.dto.BooksDTO;
import com.book.store.service.BooksManagementService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BooksManagementController {

    @Autowired
    BooksManagementService booksManagementService;
    @GetMapping("/books")
    public ResponseEntity<Object> getBooks() throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.getBooks(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/books/{idOrName}") //Let's use by ID as well
    public ResponseEntity<Object> getBooksById(@PathVariable Object id) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.getBooksById(id),HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/books")
    public ResponseEntity<String> createBooks(@RequestBody BooksDTO booksDTO, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.createBooks(booksDTO,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/books")
    public ResponseEntity<String> updateBooks(@RequestBody BooksDTO booksDTO, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.updateBooks(booksDTO,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/books")
    public ResponseEntity<String> deleteBooks(@RequestBody BooksRequest booksRequest, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.deleteBooks(booksRequest,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
