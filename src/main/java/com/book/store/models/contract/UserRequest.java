package com.book.store.models.contract;


import lombok.*;
import lombok.experimental.FieldNameConstants;


@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldNameConstants
@Builder
public class UserRequest {

    private Integer id;

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private Boolean isActiveMember;

    private Boolean isAdmin;

}
