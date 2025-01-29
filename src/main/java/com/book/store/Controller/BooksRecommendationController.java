package com.book.store.Controller;

import com.book.store.service.BooksRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksRecommendationController {
    @Autowired
    BooksRecommendationService booksRecommendationService;
    @GetMapping("/books-recommendation/{userId}")
    public ResponseEntity<Object> getBooksReview(@PathVariable int userId){
        try{
            return new ResponseEntity<>(this.booksRecommendationService.getBooksRecommendation(userId), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
