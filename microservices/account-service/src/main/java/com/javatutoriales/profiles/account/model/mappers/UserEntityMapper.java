package com.javatutoriales.profiles.account.model.mappers;

import com.javatutoriales.profiles.account.model.User;
import com.javatutoriales.profiles.account.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper( unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {
    User entityToModel(UserEntity userEntity);

    UserEntity modelToEntity(User user);
}
