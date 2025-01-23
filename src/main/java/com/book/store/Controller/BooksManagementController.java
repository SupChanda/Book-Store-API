package com.book.store.Controller;

import com.book.store.models.domain.Books;
import com.book.store.models.domain.Books_Purchase;
import com.book.store.service.BooksManagementService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BooksManagementController {

    @Autowired
    BooksManagementService booksManagementService;
    @GetMapping("/books")
    public ResponseEntity<List<Books>> getBooks() throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.getBooks(), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @GetMapping("/books/{title}") //Let's use by ID as well
    public ResponseEntity<Books> getBookByTitle(@PathVariable String title) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.getBookByTitle(title), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex);
        }
    }



    @PostMapping("/books")
    public ResponseEntity<String> createBooks(@RequestBody Books bk, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.createBooks(bk,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @PutMapping("/books/{title}")// Add by ID as well
    public ResponseEntity<String> updateBooks(@PathVariable String title, @RequestBody Books bk, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.updateBooks(title,bk,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @DeleteMapping("/books/{title}") // Add id as well
    public ResponseEntity<String> deleteBooks(@PathVariable String title, @RequestHeader String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksManagementService.deleteBooks(title,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

}
