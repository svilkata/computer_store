package bg.softuni.computer_store.config.mapper;


import bg.softuni.computer_store.model.binding.UserRegisterDTO;
import bg.softuni.computer_store.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "active", constant = "true")
    UserEntity userDtoToUserEntity(UserRegisterDTO registerDto);

}
