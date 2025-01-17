package com.book.store.Controller;

import com.book.store.models.domain.Books;
import com.book.store.service.impl.BooksManagementService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BooksManagementController {

    @Autowired
    BooksManagementService bkSrvc;
    @GetMapping("/Books")
    public ResponseEntity<List<Books>> getBooks() throws BadRequestException {
        try{
            return new ResponseEntity<>(this.bkSrvc.getBooks(), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }


    @PostMapping("/Books")
    public ResponseEntity<String> createBooks(@RequestBody Books bk, @RequestHeader String admin) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.bkSrvc.createBooks(bk,admin), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @PutMapping("/Books/{title}")
    public ResponseEntity<String> updateBooks(@PathVariable String title, @RequestBody Books bk, @RequestHeader String admin) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.bkSrvc.updateBooks(title,bk,admin), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

    @DeleteMapping("/Books/{title}")
    public ResponseEntity<String> deleteBooks(@PathVariable String title, @RequestHeader String admin) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.bkSrvc.deleteBooks(title,admin), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }

}
