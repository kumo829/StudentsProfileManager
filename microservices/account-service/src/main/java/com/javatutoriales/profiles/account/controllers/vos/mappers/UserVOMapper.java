package com.javatutoriales.profiles.account.controllers.vos.mappers;


import com.javatutoriales.profiles.account.controllers.vos.UserRequest;
import com.javatutoriales.profiles.account.controllers.vos.UserResponse;
import com.javatutoriales.profiles.account.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserVOMapper {
    @Mapping(target = "id", ignore = true)
    User requestToModel(UserRequest userRequest);

    UserResponse modelToResponse(User user);
}
