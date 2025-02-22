package com.book.store.dao.impl;

import com.book.store.dao.UserDao;
import com.book.store.models.contract.UserRequest;
//import com.book.store.models.domain.User;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.catalina.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl extends GenericDaoImpl<BookUser> implements UserDao {
    @Autowired
    UserMapper userMapper;

    private String queryString;
    private final String queryTemplateByUserName = "FROM ${User} b WHERE b.${userName} = :userName";
    private final String queryTemplateByUserId = "FROM ${User} b WHERE b.${id} = :userId";

    private final String updateQueryTemplate = "UPDATE ${User} b SET b.${userName} = :userName, " +
            "b.${password} = :password, b.${firstName} = :firstName, b.${lastName} = :lastName, " +
            "b.${isActiveMember} = :isActiveMember, b.${isAdmin} = :isAdmin WHERE b.${id} = :userId";
    private final String deleteQueryTemplate = "DELETE " + queryTemplateByUserId;

    private Map<String, Object> templateValues;
    private Map<String, Object> queryParams;
    @Override
    public Object getUsrByUserName(String userName) throws BadRequestException {

        templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);

        queryParams = new HashMap<>();
        queryParams.put("userName", userName);

        queryString = generateQueryString(queryTemplateByUserName, templateValues);
        return getHQLSingleQueryResultSet(queryString, queryParams);

    }

    @Override
    public Object getUsrByUserId(Integer userId) throws BadRequestException {

        templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("id", BookUser.Fields.id);

        queryParams = new HashMap<>();
        queryParams.put("userId", userId);

        queryString = generateQueryString(queryTemplateByUserId, templateValues);
        return getHQLSingleQueryResultSet(queryString, queryParams);

    }

    @Override
    public boolean isUserAdmin(String currentUser) throws BadRequestException {
        templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);
        templateValues.put("isAdmin", BookUser.Fields.isAdmin);

        queryParams = new HashMap<>();
        queryParams.put("userName", currentUser);

        queryString = generateQueryString(queryTemplateByUserName, templateValues);

        BookUser user = (BookUser) getHQLSingleQueryResultSet(queryString, queryParams);
        return user.getIsAdmin();
    }

    @Override
    public boolean addUser(String userName, UserRequest userRequest) {
        templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);

        queryParams = new HashMap<>();
        queryParams.put("userName", userName);

        queryString = generateQueryString(queryTemplateByUserName, templateValues);
        Long userCount = (Long) getHQLQueryCount(queryString, queryParams);
        if (userCount == 0) {
            BookUser user = userMapper.toUserFromUserRequest(userRequest);
            saveOrUpdate(user);
            return true;
        }
        return false;
    }

    @Override
    public void updateUser(Integer userId, UserRequest userRequest) throws BadRequestException {

        templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);
        templateValues.put("password", BookUser.Fields.password);
        templateValues.put("firstName", BookUser.Fields.firstName);
        templateValues.put("lastName", BookUser.Fields.lastName);
        templateValues.put("isActiveMember", BookUser.Fields.isActiveMember);
        templateValues.put("isAdmin", BookUser.Fields.isAdmin);
        templateValues.put("id", BookUser.Fields.id);

        queryString = generateQueryString(updateQueryTemplate, templateValues);

        queryParams = new HashMap<>();
        queryParams.put("userName", userRequest.getUserName());
        queryParams.put("password", userRequest.getPassword());
        queryParams.put("firstName", userRequest.getFirstName());
        queryParams.put("lastName", userRequest.getLastName());
        queryParams.put("isActiveMember", userRequest.getIsActiveMember());
        queryParams.put("isAdmin", userRequest.getIsAdmin());
        queryParams.put("userId", userId);

        updateOrDeleteObject(queryString, queryParams);


    }

    @Override
    public void deleteUser(Integer userId, String currentUser) throws BadRequestException {
        templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("id", BookUser.Fields.id);
        queryString = generateQueryString(deleteQueryTemplate, templateValues);

        queryParams = new HashMap<>();
        queryParams.put("userId", userId);

        updateOrDeleteObject(queryString, queryParams);
    }


}
