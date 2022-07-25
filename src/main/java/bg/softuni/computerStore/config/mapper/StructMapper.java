package bg.softuni.computerStore.config.mapper;

import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  //може да бъде expose-вано като Spring bean
public interface StructMapper {

    @Mapping(target = "itemId")
    ComputerViewGeneralModel computerEntityToComputerSalesViewGeneralModel(ComputerEntity computerEntity);

    @Mapping(target = "itemId")
    MonitorViewGeneralModel monitorEntityToMonitorViewGeneralModel(MonitorEntity monitorEntity);

//    @Mapping(source = "currentQuantity",target = "newQuantityToAdd")
//    AddUpdateComputerBindingDTO computerEntityToAddUpdateComputerBindingDTO(ComputerEntity computerEntity);

}

