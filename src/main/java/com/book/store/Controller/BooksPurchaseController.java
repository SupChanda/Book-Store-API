package com.book.store.Controller;

import com.book.store.models.domain.BooksPurchased;
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

    //@Autowired
    BooksPurchaseService booksPurchaseService;
    @GetMapping("/books-transaction")
    public ResponseEntity<List<BooksPurchased>> getPurchasedBooksDetails() throws BadRequestException {//TO DO: Delete the underscore
        try{
            return new ResponseEntity<>(this.booksPurchaseService.getPurchasedBooksDetails(), HttpStatus.OK);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
    @PostMapping("/books-transaction")
    public ResponseEntity<String> addBookPurchasedOrRentDetails(
                                        @RequestHeader      String  title // BETTER TO DO USING ID
                                        ,@RequestHeader     String transactionType
                                        ,@RequestHeader     int quantity
                                        ,@RequestHeader     String currentUser) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksPurchaseService.addBookPurchasedOrRentDetails(title,transactionType,quantity,currentUser), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/return/books-transaction/{bookId}")
    public ResponseEntity<String> UpdateBookDetailsOnReturn(
            @PathVariable      int bookId
            ,@RequestHeader     int userId) throws BadRequestException {
        try{
            return new ResponseEntity<>(this.booksPurchaseService.UpdateBookDetailsOnReturn(bookId,userId), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
