package com.book.store.service.impl;


import static org.mockito.Mockito.*;

import com.book.store.Repository.UserRepository;
import com.book.store.dao.UserDao;
import com.book.store.dao.impl.UserDaoImpl;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

//    @Mock
//    private UserDaoImpl userDaoImpl;

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserDTO userDTO;

    //@InjectMocks
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

        when(userDao.getUsrByUserName(bookUser.getUserName())).thenReturn(bookUser);

        userService.getUsrByUserName(bookUser.getUserName());

        verify(userDao, times(1)).getUsrByUserName(bookUser.getUserName());


    }
    @Test
    public void getUsrByUserIdTest() throws BadRequestException {
        BookUser bookUser = Generators.generateTestUser();

        when(userDao.getUsrByUserId(bookUser.getId())).thenReturn(bookUser);

        userService.getUsrByUserId(bookUser.getId());

        verify(userDao, times(1)).getUsrByUserId(bookUser.getId());


    }
    @Test
    public void addUserTest() throws BadRequestException{
        UserDTO userDTO = Generators.generateTestUserDTO();
        UserRequest userRequest = Generators.generateTestUserRequest();

        when(userMapper.toUserRequest(userDTO)).thenReturn(userRequest);
        when(userDao.addUser(userRequest.getUserName(),userRequest)).thenReturn(true);

        userService.addUser(userRequest);
        userMapper.toUserRequest(userDTO);
        userDao.addUser(userRequest.getUserName(),userRequest);

        verify(userMapper,times(1)).toUserRequest(userDTO);
        verify(userDao,times(2)).addUser(userRequest.getUserName(),userRequest);

    }

    @Test
    public void updateUserTest() throws BadRequestException{
        UserDTO userDTO = Generators.generateTestUserDTO();
        UserRequest userRequest = Generators.generateTestUserRequest();

        when(userDao.isUserAdmin("Admin")).thenReturn(true);
        when(userMapper.toDTO((BookUser) userDao.getUsrByUserId(userRequest.getId()))).thenReturn(userDTO);

        userMapper.toUserRequest(userDTO);
        userService.updateUser(userRequest,"Admin");


        verify(userMapper,times(1)).toUserRequest(userDTO);
        verify(userDao,times(1)).isUserAdmin("Admin");
        verify(userDao,times(2)).getUsrByUserId(userRequest.getId());// getUserByUserId is called twice which is this is 2
        verify(userMapper,times(1)).toDTO((BookUser) userDao.getUsrByUserId(userRequest.getId()));
        verify(userDao,times(1)).updateUser(userRequest.getId(),userRequest);

    }

    @Test
    public void deleteUserTest() throws BadRequestException{
        BookUser user = Generators.generateTestUser();
        UserDTO userDTO = Generators.generateTestUserDTO();
        int userId = user.getId();

        when(userMapper.toDTO(user)).thenReturn(userDTO);
        when(userDao.getUsrByUserId(user.getId())).thenReturn(user);
        when(userDao.getUsrByUserName(user.getUserName())).thenReturn(user);

        userService.deleteUser(userId,"Admin");


        verify(userMapper,times(2)).toDTO(user);
        verify(userDao,times(1)).getUsrByUserId(userId);
        verify(userDao,times(1)).getUsrByUserName(user.getUserName());
        verify(userDao,times(1)).deleteUser(userId,"Admin");

    }

}
