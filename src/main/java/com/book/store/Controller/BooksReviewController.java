package com.book.store.Controller;

import com.book.store.models.contract.BooksReviewRequest;
import com.book.store.models.domain.BooksReview;
import com.book.store.service.BooksReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
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
            return new ResponseEntity<>(this.booksReviewService.getBooksReviewByBookId(bookId), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/books-reviews")
    public ResponseEntity<Object> addBooksReview(@RequestBody BooksReviewRequest booksReviewRequest){
        try{
            return new ResponseEntity<>(this.booksReviewService.addBooksReview(booksReviewRequest), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
