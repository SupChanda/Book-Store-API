package com.book.store.service.impl;

import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.BooksPurchaseDao;
import com.book.store.dao.UserDao;
import com.book.store.helpers.Generators;
import com.book.store.models.contract.BooksPurchasedRequest;
import com.book.store.models.contract.BooksRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.BooksPurchasedDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.models.mappers.BooksPurchasedMapper;
import org.apache.catalina.User;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BooksManagementImplTest {

    @Mock
    BooksDTO booksDTO;
    @Mock
    BooksRequest booksRequest;

    @Mock
    BooksManagementDao booksManagementDao;
    @Mock
    BooksMapper booksMapper;
    @Mock
    UserDao userDao;

    @InjectMocks
    BooksManagementServiceImpl booksManagementService;

    @Test
    public void getBooksTest() throws BadRequestException {
        Books books1 = Generators.generateTestBooks1();
        Books books2 = Generators.generateTestBooks2();
        List<Books> booksList = new ArrayList<>();
        booksList.add(books1);
        booksList.add(books2);
        BooksDTO booksDTO1 = Generators.generateTestBooksDTO1();
        BooksDTO booksDTO2 = Generators.generateTestBooksDTO2();
        List<BooksDTO> booksDTOList = new ArrayList<>();
        booksDTOList.add(booksDTO1);
        booksDTOList.add(booksDTO2);

        when(booksManagementDao.getBooksList()).thenReturn(booksList);
        when(booksMapper.toDTOList(booksList)).thenReturn(booksDTOList);

        List<BooksDTO> booksList1 = booksManagementService.getBooks();

        verify(booksManagementDao,times(1)).getBooksList();
        verify(booksMapper,times(1)).toDTOList(booksList);
        Assertions.assertEquals(booksDTOList,booksList1);
    }

    @Test
    public void getBooksByIdTest() throws BadRequestException {
        Books books1 = Generators.generateTestBooks1();
        BooksDTO booksDTO1 = Generators.generateTestBooksDTO1();
        when(booksManagementDao.getBooksById(books1.getId())).thenReturn(books1);
        when(booksMapper.toDTO(books1)).thenReturn(booksDTO1);

        Object obj = booksManagementService.getBooksById(books1.getId());

        verify(booksManagementDao,times(1)).getBooksById(books1.getId());
        verify(booksMapper,times(1)).toDTO(books1);
        Assertions.assertEquals(obj,booksDTO1);
    }

    @Test
    public void createBooksTest() throws BadRequestException {
        BookUser user = Generators.generateTestUser();
        BooksDTO booksDTO1 = Generators.generateTestBooksDTO1();
        when(userDao.getUsrByUserName(user.getUserName())).thenReturn(user);
        when(userDao.isUserAdmin(user.getUserName())).thenReturn(true);
        when(booksManagementDao.createBooks(booksDTO1,user.getUserName())).thenReturn(true);

        String response = booksManagementService.createBooks(booksDTO1,user.getUserName());

        verify(userDao,times(1)).getUsrByUserName(user.getUserName());
        verify(userDao,times(1)).isUserAdmin(user.getUserName());
        verify(booksManagementDao,times(1)).createBooks(booksDTO1, user.getUserName());

        Assertions.assertEquals("Quiet Place has been added",response);
    }

    @Test
    public void updateBooksTest() throws BadRequestException {
        BookUser user = Generators.generateTestUser();
        BooksDTO booksDTO1 = Generators.generateTestBooksDTO1();

        when(userDao.getUsrByUserName(user.getUserName())).thenReturn(user);
        when(userDao.isUserAdmin(user.getUserName())).thenReturn(true);
        when(booksManagementDao.getBooksById(user.getId())).thenReturn(user.getId());
        when(booksManagementDao.updateBooks(booksDTO1,user.getUserName())).thenReturn("true");

        String response  = booksManagementService.updateBooks(booksDTO1,user.getUserName());

        verify(userDao,times(1)).getUsrByUserName(user.getUserName());
        verify(userDao,times(1)).isUserAdmin(user.getUserName());
        verify(booksManagementDao,times(1)).getBooksById(user.getId());
        verify(booksManagementDao,times(1)).updateBooks(booksDTO1, user.getUserName());
        Assertions.assertEquals("true",response);
    }

    @Test
    public void deleteBooksTest() throws BadRequestException {
        BookUser user = Generators.generateTestUser();
        BooksRequest booksRequest1 = Generators.generateTestBooksRequest();

        when(userDao.isUserAdmin(user.getUserName())).thenReturn(true);
        when(booksManagementDao.getBooksById(user.getId())).thenReturn(user.getId());
        when(booksManagementDao.deleteBooks(booksRequest1.getId(),user.getUserName())).thenReturn("true");

        String response = booksManagementService.deleteBooks(booksRequest1,user.getUserName());

        verify(userDao,times(1)).isUserAdmin(user.getUserName());
        verify(booksManagementDao,times(1)).getBooksById(user.getId());
        verify(booksManagementDao,times(1)).deleteBooks(booksRequest1.getId(), user.getUserName());

        Assertions.assertEquals("true",response);
    }

}
