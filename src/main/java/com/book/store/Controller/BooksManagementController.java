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

//    @GetMapping("/books/title/{title}") //Let's use by ID as well
//    public ResponseEntity<Object> getBooksById(@PathVariable int id) throws BadRequestException {
//        try{
//            return new ResponseEntity<>(this.booksManagementService.getBooksByIdOrName(id),HttpStatus.OK);
//            //return new ResponseEntity<>(this.booksManagementService.getBookByTitle(title), HttpStatus.OK);
//        }catch(Exception ex){
//            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/books/{idOrName}") //Let's use by ID as well
    public ResponseEntity<Object> getBooksById(@PathVariable Object idOrName) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.getBooksByIdOrName(idOrName),HttpStatus.OK);
            //return new ResponseEntity<>(this.booksManagementService.getBookByTitle(title), HttpStatus.OK);
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

//    @PutMapping("/books/{title}")// Add by ID as well
//    public ResponseEntity<String> updateBooks(@PathVariable String title, @RequestBody Books bk, @RequestHeader String currentUser) throws BadRequestException {
//        try{
//            return new ResponseEntity<>(this.booksManagementService.updateBooks(title,bk,currentUser), HttpStatus.OK);
//        }catch(Exception ex){
//            throw new BadRequestException(ex.getMessage());
//        }
//    }

    @PutMapping("/books")// Add by ID as well
    public ResponseEntity<String> updateBooks(@RequestBody BooksDTO booksDTO, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.updateBooks(booksDTO,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/books") // Add id as well
    public ResponseEntity<String> deleteBooks(@RequestBody BooksRequest booksRequest, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.deleteBooks(booksRequest,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
