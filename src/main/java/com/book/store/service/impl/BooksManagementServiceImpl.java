package com.book.store.service.impl;

import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.UserDao;
import com.book.store.models.contract.BooksRequest;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.service.BooksManagementService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksManagementServiceImpl implements BooksManagementService {

    @Autowired
    BooksMapper booksMapper;
    @Autowired
    BooksManagementDao booksManagementDao;
    @Autowired
    UserDao userDao;

    @Override
    public List<BooksDTO> getBooks() throws BadRequestException {
        List<Books> booksList = (List<Books>) booksManagementDao.getBooksList();
        return this.booksMapper.toDTOList(booksList);
    }
    public Object getBooksById(Object obj) throws BadRequestException {
        try {
            return this.booksMapper.toDTO((Books) booksManagementDao.getBooksById(obj));
        } catch (Exception ex) {
            throw new BadRequestException("Invalid id: " + obj);
        }
    }

    public Object getBooksByIdOrName(Object obj) throws BadRequestException {
        try {
            return this.booksMapper.toDTO((Books) booksManagementDao.getBooksByIdOrName(obj));
        } catch (Exception ex) {
            try {
                obj = Integer.parseInt((String) obj);
                throw new BadRequestException("Invalid id: " + obj);
            } catch (Exception ex1) {
                throw new BadRequestException("Invalid name: " + obj);
            }
        }
    }

    @Override
    public String createBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException {
        try {
            try {
                userDao.getUsrByUserName(currentUser);
            } catch (Exception ex) {
                throw new BadRequestException("Invalid name: " + currentUser);
            }
            if (!userDao.isUserAdmin(currentUser)) {
                throw new BadRequestException(currentUser + " is not an Admin User!");
            } else if (booksManagementDao.createBooks(booksDTO, currentUser)) {
                return booksDTO.getTitle() + " has been added";
            }
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return booksDTO.getTitle() + " has been added";
    }

    @Override
    @Transactional
    public String updateBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException {
        try {
            try {
                userDao.getUsrByUserName(currentUser);
            } catch (Exception ex) {
                throw new BadRequestException("Invalid name: " + currentUser);
            }
            if (!userDao.isUserAdmin(currentUser)) {
                throw new BadRequestException(currentUser + " is not an Admin User!");
            } else if (booksManagementDao.getBooksByIdOrName(booksDTO.getId()) == null) {
                throw new BadRequestException("Invalid id: " + booksDTO.getId());
            }
            return booksManagementDao.updateBooks(booksDTO, currentUser);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public String deleteBooks(BooksRequest booksRequest, String currentUser) throws BadRequestException {
        try {
            if (!userDao.isUserAdmin(currentUser)) {
                throw new BadRequestException(currentUser + " is not an Admin User!");
            }
            if (booksManagementDao.getBooksByIdOrName(booksRequest.getId()) == null) {
                throw new BadRequestException("Invalid id: " + booksRequest.getId());
            }
            return booksManagementDao.deleteBooks(booksRequest.getId(), currentUser);
        } catch (Exception ex) {
            throw new BadRequestException("Invalid id:" + booksRequest.getId());
        }
    }
}
