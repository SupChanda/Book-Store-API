package com.book.store.service.impl;
//
//import com.book.store.Repository.UserRepository;
//import com.book.store.dao.GenericDao;
//import com.book.store.dao.UserDao;
//import com.book.store.dao.impl.GenericDaoImpl;
//import com.book.store.dao.impl.UserDaoImpl;
//import com.book.store.helpers.Generators;
//import com.book.store.models.contract.UserRequest;
//import com.book.store.models.domain.BookUser;
//import com.book.store.models.dto.UserDTO;
//import com.book.store.models.mappers.UserMapper;
//import com.book.store.service.UserService;
//import jakarta.persistence.EntityManager;
//import org.apache.coyote.BadRequestException;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.book.store.Repository.UserRepository;
import com.book.store.dao.UserDao;
import com.book.store.helpers.Generators;
import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.UserDTO;
import com.book.store.models.mappers.UserMapper;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// Change to JUnit 5
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private UserRepository userRepository;
//    @Mock
//    private UserDaoImpl userDaoImpl;

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserDTO userDTO;

    @InjectMocks
    //@Spy
    private UserServiceImpl userService;

    @BeforeEach
    public void setup(){
        //genericDaoImpl.getSession();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUsrByUserNameTest() throws BadRequestException {
        BookUser bookUser = Generators.generateTestUser();
        when(userService.getUsrByUserName(bookUser.getUserName())).thenReturn(bookUser.getUserName());

        String userName = (String) userService.getUsrByUserName(bookUser.getUserName());
        assertEquals("admin",userName);


    }
    @Test
    public void getUsrByUserIDTest() throws BadRequestException {
        BookUser bookUser = Generators.generateTestUser();
        when(userService.getUsrByUserId(bookUser.getId())).thenReturn(bookUser.getId());

        int userId = (Integer) userService.getUsrByUserId(bookUser.getId());
        assertEquals(1,userId);


    }
    @Test
    public void AddUserTest() throws BadRequestException {
        UserRequest userRequest = Generators.generateTestUserRequest();
        userService.addUser(userRequest);
        when(userService.getUsrByUserId(userRequest.getId())).thenReturn(userRequest.getId());

        int userId = (Integer) userService.getUsrByUserId(userRequest.getId());
        assertEquals(2,userId);


    }

    @Test
    public void UpdateUserTest() throws BadRequestException {
        BookUser bookUser = Generators.generateTestUser();
        UserRequest userRequest = Generators.generateTestUserRequest();
        userService.updateUser(userRequest,bookUser.getUserName());

        when(userService.getUsrByUserName(bookUser.getUserName())).thenReturn(bookUser.getUserName());

        String userName = (String) userService.getUsrByUserName(bookUser.getUserName());
        assertEquals("Admin",userName);

        verify(userDao).getUsrByUserName(userName);
        verify(userDao).isUserAdmin(bookUser.getUserName());


    }

    @Test
    public void DeleteUserTest() throws BadRequestException {
        BookUser bookUser = Generators.generateTestUser();
        UserRequest userRequest = Generators.generateTestUserRequest();
//        System.out.println(bookUser.getUserName());
//        System.out.println(userRequest.getUserName());
        userDao.deleteUser(bookUser.getId(),userRequest.getUserName());

        when(userDao.getUsrByUserName(bookUser.getUserName())).thenReturn(bookUser.getUserName());
        String userName = (String) userDao.getUsrByUserName(bookUser.getUserName());
        assertEquals("Admin",userName);
        verify(userDao).getUsrByUserName(bookUser.getUserName());
        verify(userDao).getUsrByUserName(userRequest.getUserName());


        when(userDao.getUsrByUserId(bookUser.getId())).thenReturn(bookUser.getId());
        Integer userId = (Integer) userDao.getUsrByUserId(bookUser.getId());
        assertEquals(1,userId);
        verify(userDao).getUsrByUserId(bookUser.getId());

        when(userDao.getUsrByUserId(userRequest.getId())).thenReturn(userRequest.getId());
        userId = (Integer) userDao.getUsrByUserId(userRequest.getId());
        assertEquals(2,userId);
        verify(userDao).getUsrByUserId(userRequest.getId());


    }
}
