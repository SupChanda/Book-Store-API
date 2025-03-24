package com.book.store.service.impl;

import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.BooksPurchaseDao;
import com.book.store.helpers.Generators;
import com.book.store.models.contract.BooksPurchasedRequest;
import com.book.store.models.contract.BooksRequest;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.BooksPurchasedDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.models.mappers.BooksPurchasedMapper;
import org.apache.coyote.BadRequestException;
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
    Books books;

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

        booksManagementService.getBooks();

        verify(booksManagementDao,times(1)).getBooksList();
        verify(booksMapper,times(1)).toDTOList(booksList);

    }

    @Test
    public void getBooksByIdOrNameTest() throws BadRequestException {
        Books books1 = Generators.generateTestBooks1();
        BooksDTO booksDTO1 = Generators.generateTestBooksDTO1();

        when(booksManagementDao.getBooksByIdOrName(books1.getTitle())).thenReturn(books1.getTitle());

        booksManagementService.getBooksByIdOrName(books1.getTitle());

        verify(booksManagementDao,times(1)).getBooksByIdOrName(books1.getTitle());
        verify(booksMapper,times(1)).toDTO(books1);

    }


}
