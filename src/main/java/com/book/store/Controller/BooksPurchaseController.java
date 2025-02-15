package com.book.store.Controller;

import com.book.store.models.contract.BooksPurchasedRequest;
import com.book.store.models.domain.BooksPurchased;
import com.book.store.models.dto.BooksPurchasedDTO;
import com.book.store.service.BooksPurchaseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class BooksPurchaseController {

    @Autowired
    BooksPurchaseService booksPurchaseService;
    @GetMapping("/books-transaction")
    public ResponseEntity<Object> getPurchasedBooksDetails() throws BadRequestException {//TO DO: Delete the underscore
        try{
            System.out.println(" In Books Purchased Controller");
            return new ResponseEntity<>(this.booksPurchaseService.getPurchasedBooksDetails(), HttpStatus.OK);
        }catch(Exception ex){
            //throw new BadRequestException(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/books-transaction/{id}")
    public Object getPurchasedBooksDetails(@PathVariable int id) throws BadRequestException {//TO DO: Delete the underscore
        try{
            System.out.println(" In Books Purchased Controller");
            return new ResponseEntity<>(this.booksPurchaseService.getPurchasedBooksDetailsById(id), HttpStatus.OK);
        }catch(Exception ex){
            //throw new BadRequestException(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/books-transaction")
    public ResponseEntity<String> addBookPurchasedOrRentDetails(@RequestBody BooksPurchasedRequest booksPurchasedRequest) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksPurchaseService.addBookPurchasedOrRentDetails(booksPurchasedRequest), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/return/books-transaction")
    public ResponseEntity<String> UpdateBookDetailsOnReturn(@RequestBody BooksPurchasedDTO booksPurchasedDTO) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksPurchaseService.UpdateBookDetailsOnReturn(booksPurchasedDTO), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
