package com.book.store.service.impl;

import com.book.store.dao.BooksRecommendationDao;
import com.book.store.models.domain.Books;
import com.book.store.service.BooksRecommendationService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksRecommendationServiceImpl implements BooksRecommendationService {
    @Autowired
    BooksRecommendationDao booksRecommendationDao;
    @Override
    public List<Books> getBooksRecommendation(int userId) throws BadRequestException {
        try{
            return booksRecommendationDao.getBooksRecommendation(userId);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
    }
}
