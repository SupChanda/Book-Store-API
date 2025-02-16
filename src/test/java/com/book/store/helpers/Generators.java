package com.book.store.helpers;

import com.book.store.models.domain.BookUser;

public class Generators {

    public static BookUser generateTestUser(){
        return BookUser.builder()
            .id(1)
            .userName("Sam")
            .password("123445")
            .firstName("Sam")
            .lastName("Mort")
            .isActiveMember(true)
            .isAdmin(true)
            .build();

    }
}
