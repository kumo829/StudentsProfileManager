package com.javatutoriales.profiles.account.controllers.vos.mappers;


import com.javatutoriales.profiles.account.controllers.vos.UserRequest;
import com.javatutoriales.profiles.account.controllers.vos.UserResponse;
import com.javatutoriales.profiles.account.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserVOMapper {
    User requestToModel(UserRequest userRequest);

    UserResponse modelToResponse(User user);
}
