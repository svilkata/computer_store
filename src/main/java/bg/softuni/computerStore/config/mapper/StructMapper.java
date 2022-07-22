package bg.softuni.computerStore.config.mapper;

import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  //може да бъде expose-вано като Spring bean
public interface StructMapper {

    @Mapping(target = "itemId")
    ComputerViewGeneralModel computerEntityToComputerViewGeneralModel(ComputerEntity computerEntity);

//    @Mapping(target = "id")
//    TyreCreatedModifiedResponseJsonDTO tyreEntityToTyreCreatedModifiedResponseJsonDTO(TyreEntity tyreEntity);

}

