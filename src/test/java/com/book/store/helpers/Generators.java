package com.book.store.helpers;

import com.book.store.models.contract.BooksRequest;
import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.domain.Books;
import com.book.store.models.dto.BooksDTO;
import com.book.store.models.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

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
    public static Books generateTestBooks1(){
        return Books.builder()
                .id(1)
                .title("Discipline")
                .author("Admin")
                .genre("Drama")
                .price(35)
                .rentalFee(4)
                .noOfCopies(2)
                .build();


    }
    public static Books generateTestBooks2(){
        return Books.builder()
                .id(1)
                .title("MockingBird")
                .author("Admin")
                .genre("Drama")
                .price(35)
                .rentalFee(4)
                .noOfCopies(2)
                .build();


    }

    public static BooksDTO generateTestBooksDTO1(){
        return BooksDTO.builder()
                .id(1)
                .title("Quiet Place")
                .author("Admin")
                .genre("Comedy")
                .price(35)
                .rentalFee(4)
                .noOfCopies(2)
                .build();


    }
    public static BooksDTO generateTestBooksDTO2(){
        return BooksDTO.builder()
                .id(1)
                .title("Mockingbird")
                .author("Admin")
                .genre("Comedy")
                .price(35)
                .rentalFee(4)
                .noOfCopies(2)
                .build();


    }

    public static BooksRequest generateTestBooksRequest(){
        return BooksRequest.builder()
                .id(1)
                .build();


    }


}
