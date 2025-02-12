package com.book.store.dao.impl;

import com.book.store.dao.BooksManagementDao;
import com.book.store.dao.GenericDao;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import lombok.Generated;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class BooksManagementDaoImpl  extends GenericDaoImpl<Books> implements BooksManagementDao {

    private final String queryFromTemplate = "FROM ${Books}";
    private final String queryWhereTemplate = "WHERE ${IdOrName} = :userNameOrID";
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


}
