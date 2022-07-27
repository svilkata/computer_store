package bg.softuni.computerStore.config.mapper;

import bg.softuni.computerStore.model.entity.cloudinary.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  //може да бъде expose-вано като Spring bean
public interface StructMapper {
    @Mapping(source = "photo", target = "photoUrl")
    ComputerViewGeneralModel computerEntityToComputerSalesViewGeneralModel(ComputerEntity computerEntity);

    @Mapping(source = "photo", target = "photoUrl")
    MonitorViewGeneralModel monitorEntityToMonitorViewGeneralModel(MonitorEntity monitorEntity);

    //using default mapper method
    default String map(PictureEntity photo){
        return photo == null ? "" :  photo.getUrl();
    }

//    @Mapping(source = "currentQuantity",target = "newQuantityToAdd")
//    AddUpdateComputerBindingDTO computerEntityToAddUpdateComputerBindingDTO(ComputerEntity computerEntity);

}

