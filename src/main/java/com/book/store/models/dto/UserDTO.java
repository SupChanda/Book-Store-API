package com.book.store.models.dto;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
@Builder
public class UserDTO {

    private Integer id;

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private Boolean isActiveMember;

    private Boolean isAdmin;

}
