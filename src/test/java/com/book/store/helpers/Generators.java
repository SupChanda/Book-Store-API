package com.book.store.helpers;

import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.UserDTO;

public class Generators {

    public static BookUser generateTestUser(){
        return BookUser.builder()
            .id(1)
            .userName("Admin")
            .password("123445")
            .firstName("Sam")
            .lastName("Mort")
            .isActiveMember(true)
            .isAdmin(true)
            .build();


    }
    public static UserDTO generateTestUserDTO(){
        return UserDTO.builder()
                .id(1)
                .userName("Admin")
                .password("123445")
                .firstName("Sam")
                .lastName("Mort")
                .isActiveMember(true)
                .isAdmin(true)
                .build();


    }
    public static UserRequest generateTestUserRequest(){
        return UserRequest.builder()
                .id(2)
                .userName("Admin")
                .password("123445")
                .firstName("Sam")
                .lastName("Mort")
                .isActiveMember(true)
                .isAdmin(true)
                .build();


    }

}
