package com.kubancevvladislav.services.mapper;

import com.kubancevvladislav.domain.DTO.UserDTO;
import com.kubancevvladislav.domain.User;
import com.kubancevvladislav.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperInterface {
    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);

    User toDomain(UserDTO dto);
}