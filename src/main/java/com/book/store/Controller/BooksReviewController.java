package com.book.store.Controller;

import com.book.store.models.domain.Books_Review;
import com.book.store.service.BooksReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BooksReviewController {
    @Autowired
    BooksReviewService booksReviewService;

    @GetMapping("/books-reviews")
    public ResponseEntity<Object> getBooksReview(){
        try{
            return new ResponseEntity<>(this.booksReviewService.getBooksReview(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/books-reviews/{bookId}")
    public ResponseEntity<Object> getBooksReviewByID(@PathVariable int bookId){
        try{
            return new ResponseEntity<>(this.booksReviewService.getBooksReviewByID(bookId), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/books-reviews")
    public ResponseEntity<Object> addBooksReview(@RequestHeader int bookId, @RequestHeader int userId,@RequestHeader String comments){
        try{
            return new ResponseEntity<>(this.booksReviewService.addBooksReview(bookId,userId,comments), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
