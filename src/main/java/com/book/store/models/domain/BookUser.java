package com.book.store.models.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "BookUser")
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@FieldNameConstants
public class BookUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "UserName",unique = true,nullable = false)
    private String userName;

    @Column(name = "Password")
    private String password;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "IsActiveMember")
    private Boolean isActiveMember;

    @Column(name = "IsAdmin")
    private Boolean isAdmin;

}
