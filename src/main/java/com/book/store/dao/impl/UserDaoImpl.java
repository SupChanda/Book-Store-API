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
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    UserMapper userMapper;

    String queryString;
    private final String queryTemplateByUserName = "FROM ${User} b WHERE b.${userName} = :userName";
    private final String queryTemplateByUserId = "FROM ${User} b WHERE b.${id} = :userId";

    private final String updateQueryTemplate = "UPDATE ${User} b SET b.${userName} = :userName, " +
            "b.${password} = :password, b.${firstName} = :firstName, b.${lastName} = :lastName, " +
            "b.${isActiveMember} = :isActiveMember, b.${isAdmin} = :isAdmin WHERE b.${id} = :userId";
    private final String deleteQueryTemplate = "DELETE " + queryTemplateByUserId;


    @Override
    public Object getUsrByUserName(String userName) throws BadRequestException {

        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userName", userName);

        String queryString = generateQueryString(queryTemplateByUserName, templateValues);
        return getHQLSingleQueryResultSet(queryString, queryParams);

    }

    @Override
    public Object getUsrByUserId(Integer userId) throws BadRequestException {

        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("id", BookUser.Fields.id);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", userId);

        String queryString = generateQueryString(queryTemplateByUserId, templateValues);
        return getHQLSingleQueryResultSet(queryString, queryParams);

    }

    @Override
    public boolean isUserAdmin(String currentUser) throws BadRequestException {// should only interact with the service
        //BookUser user = (BookUser) getUsrByUserName(currentUser);
        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);
        templateValues.put("isAdmin", BookUser.Fields.isAdmin);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userName", currentUser);

        String queryString = generateQueryString(queryTemplateByUserName, templateValues);

        BookUser user = (BookUser) getHQLSingleQueryResultSet(queryString, queryParams);
//        if(!user.getIsAdmin() ){
//            System.out.println("not admin");
//            return false;
//        }
        System.out.println("admin");
        return user.getIsAdmin();
    }

    @Override
    public boolean addUser(String userName, UserRequest userRequest) {
        //System.out.println("Yes Here");
        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userName", userName);

        String queryString = generateQueryString(queryTemplateByUserName, templateValues);
        Long userCount = (long) getHQLQueryCount(queryString, queryParams);
        //System.out.println("userCount: " + userCount);

        if (userCount == 0) {
            BookUser user = userMapper.toUserFromUserRequest(userRequest);
            saveOrUpdate(user);
            return true;
        }
        return false;
    }

    @Override
    public void updateUser(Integer userId, UserRequest userRequest) throws BadRequestException {

        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("userName", BookUser.Fields.userName);
        templateValues.put("password", BookUser.Fields.password);
        templateValues.put("firstName", BookUser.Fields.firstName);
        templateValues.put("lastName", BookUser.Fields.lastName);
        templateValues.put("isActiveMember", BookUser.Fields.isActiveMember);
        templateValues.put("isAdmin", BookUser.Fields.isAdmin);
        templateValues.put("id", BookUser.Fields.id);

        queryString = generateQueryString(updateQueryTemplate, templateValues);

        Map<String, Object> queryParams = new HashMap<>();
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
        //private final String deleteQueryTemplate = "DELETE FROM ${User} b WHERE b.${id} = :userId";
        Map<String, Object> templateValues = new HashMap<>();
        templateValues.put("User", BookUser.class.getName());
        templateValues.put("id", BookUser.Fields.id);

        queryString = generateQueryString(deleteQueryTemplate, templateValues);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", userId);

        updateOrDeleteObject(queryString, queryParams);
    }


}
