package com.book.store.models.domain;

import lombok.*;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//@Entity
@Table(name = "Users")
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class Users {
    @Id
    @Column(name = "userID")
    private int userID;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "Password")
    private String password;

    @Column(name = "ISAdmin")
    private Boolean isAdmin;

}
