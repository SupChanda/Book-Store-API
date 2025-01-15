package com.book.store.models.dto;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserDTO {


    private int id;

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private Boolean isAdmin;

}
