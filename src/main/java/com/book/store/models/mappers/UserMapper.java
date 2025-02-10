package com.book.store.models.mappers;

import com.book.store.models.contract.UserRequest;
import com.book.store.models.domain.BookUser;
import com.book.store.models.dto.UserDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id",source = "id")
    @Mapping(target = "userName",source = "userName")
    @Mapping(target = "password",source = "password")
    @Mapping(target = "firstName",source = "firstName")
    @Mapping(target = "lastName",source = "lastName")
    @Mapping(target = "isActiveMember",source = "isActiveMember")
    @Mapping(target = "isAdmin",source = "isAdmin")
    UserDTO toDTO(BookUser user);

    List<UserDTO> userToDTOList(List<BookUser> users);
    UserRequest toUserRequest(UserDTO userDTO);

    BookUser toUserFromUserRequest(UserRequest userRequest);


}
