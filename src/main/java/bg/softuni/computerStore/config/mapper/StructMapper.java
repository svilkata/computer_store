package bg.softuni.computerStore.config.mapper;

import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.LaptopEntity;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.model.view.product.LaptopViewGeneralModel;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  //could be exposed as Spring bean
public interface StructMapper {
    @Mapping(target = "photoUrl", source = "photo")
    ComputerViewGeneralModel computerEntityToComputerSalesViewGeneralModel(ComputerEntity computerEntity);

    @Mapping(target = "photoUrl", source = "photo")
    MonitorViewGeneralModel monitorEntityToMonitorViewGeneralModel(MonitorEntity monitorEntity);

    @Mapping(target = "photoUrl", source = "photo")
    LaptopViewGeneralModel laptopEntityToLaptopViewGeneralModel(LaptopEntity laptopEntity);

    //using default mapper method
    default String map(PictureEntity photo){
        return photo == null ? "" :  photo.getUrl();
    }

//    @Mapping(source = "currentQuantity",target = "newQuantityToAdd")
//    AddUpdateComputerBindingDTO computerEntityToAddUpdateComputerBindingDTO(ComputerEntity computerEntity);

}

