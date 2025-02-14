package com.book.store.dao.impl;

import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.GenericDao;
import com.book.store.dao.UserDao;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.BooksMapper;
import com.book.store.models.mappers.UserMapper;
import lombok.Generated;
import org.apache.catalina.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class BooksManagementDaoImpl  extends GenericDaoImpl<Books> implements BooksManagementDao {

    @Autowired
    UserDao userDao;
    @Autowired
    BooksMapper booksMapper;
    @Autowired
    UserMapper userMapper;
    private final String queryFromTemplate = "FROM ${Books} b";
    private final String queryWhereTemplate = " WHERE b.${userNameOrID} = :userNameOrID";

    private final String updateQueryTemplate = "UPDATE ${Books} b SET b.${title} = :title, " +
            "b.${author} = :author, b.${genre} = :genre, b.${rentalFee} = :rentalFee, " +
            "b.${noOfCopies} = :noOfCopies WHERE b.${id} = :id";

    private final String deleteQueryTemplate = "DELETE " + queryFromTemplate + queryWhereTemplate;
    String queryTemplate;
    String queryString;

    @Override
    public Object getBooksList() throws BadRequestException {
        Map<String,Object> templateValues = new HashMap<>();
        templateValues.put("Books", Books.class.getName());
        System.out.println("BooksDTO.Fields.class.getName() " + Books.class.getName());
        queryString = generateQueryString(queryFromTemplate,templateValues);
//        if(getHQLQueryResultSet(queryString) == null){
//            throw new BadRequestException()
//        }
        return getHQLQueryResultSet(queryString);

    }

    @Override
    public Object getBooksByIdOrName(Object obj) throws BadRequestException {// What if the book name is 1984? taken care of on line 59

        Map<String,Object> templateValues = new HashMap<>();
        templateValues.put("Books", Books.class.getName());
        try{
            if(obj instanceof String){
                obj = Integer.parseInt((String) obj);
            }
            templateValues.put("userNameOrID", Books.Fields.id);
        }catch(Exception ex){
            templateValues.put("userNameOrID", Books.Fields.title);
        }
        queryTemplate = queryFromTemplate + queryWhereTemplate;
        queryString = generateQueryString(queryTemplate,templateValues);


        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("userNameOrID",obj);

        return getHQLSingleQueryResultSet(queryString,queryParams);
    }
    @Override
    public boolean createBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException {
        try{
            //System.out.println("in DAO Impl");
            Books books = booksMapper.toBooksFromDTO(booksDTO);
            saveOrUpdate(books);
        }catch(Exception ex){
            throw new BadRequestException(ex.getMessage());
        }
        return true;
    }

    @Override
    public String updateBooks(BooksDTO booksDTO, String currentUser) throws BadRequestException {

        //Boolean isAdminUser = userMapper.toDTO((BookUser) userDao.getUsrByUserName(currentUser)).getIsAdmin();
        //System.out.println("Is Admin user ?: " + userDao.isUserAdmin(currentUser));
        if(userDao.isUserAdmin(currentUser)){
            throw new BadRequestException( currentUser + " is not an Admin User!");
        }
        getBooksByIdOrName(String.valueOf(booksDTO.getId()));

        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("Books", Books.class.getName());
        templateValues.put("title", Books.Fields.title);
        templateValues.put("author", Books.Fields.author);
        templateValues.put("genre", Books.Fields.genre);
        templateValues.put("rentalFee", Books.Fields.rentalFee);
        templateValues.put("noOfCopies", Books.Fields.noOfCopies);
        templateValues.put("id", Books.Fields.id);

        queryString = generateQueryString(updateQueryTemplate,templateValues);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("title", booksDTO.getTitle());
        queryParams.put("author",booksDTO.getAuthor());
        queryParams.put("genre",booksDTO.getGenre());
        queryParams.put("rentalFee",booksDTO.getRentalFee());
        queryParams.put("noOfCopies",booksDTO.getNoOfCopies());
        queryParams.put("id",booksDTO.getId());

        updateOrDeleteObject(queryString,queryParams);

        return  booksDTO.getTitle() + " has been updated";
    }

    @Override
    public String deleteBooks(int id, String currentUser) throws BadRequestException {

        //getBooksByIdOrName(String.valueOf(id));

        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("Books", Books.class.getName());
        templateValues.put("userNameOrID", Books.Fields.id);
        queryString = generateQueryString(deleteQueryTemplate,templateValues);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userNameOrID",id);


        updateOrDeleteObject(queryString,queryParams);
        return  "Book ID: " + id + " has been deleted";
    }

}
