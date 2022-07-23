package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AllItemsService {
    private final AllItemsRepository allItemsRepository;

    public AllItemsService(AllItemsRepository allItemsRepository) {
        this.allItemsRepository = allItemsRepository;
    }

    public Long isItemModelPresent(String model){
        Optional<ItemEntity> byModel = this.allItemsRepository.findByModel(model);

        return byModel.isPresent() ? byModel.get().getItemId() : -1L;
    }

    public ItemEntity getItemById(Long id){
        return this.allItemsRepository.findById(id).orElseThrow();
    }
}
