package bg.softuni.computerStore.repository.products;

import bg.softuni.computerStore.model.binding.product.SearchProductItemDTO;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProductItemSpecification implements Specification<ItemEntity> {
    private final SearchProductItemDTO searchProductItemDTO;
    private final String type;

    public ProductItemSpecification(SearchProductItemDTO searchProductItemDTO, String type) {
        this.searchProductItemDTO = searchProductItemDTO;
        this.type = type;
    }

    @Override
    public Predicate toPredicate(Root<ItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(cb.equal(root.get("type"), type));

        if (searchProductItemDTO.getModel() != null && !searchProductItemDTO.getModel().isBlank()) {
//            Path<String> model = root.get("model");
            Expression<String> model = root.get("model").as(String.class);

            predicate.getExpressions().add(
                    //!!!!! when we have two relationally connected tables
//                    cb.and(cb.equal(root.join("model").get("name"), searchProductItemDTO.getModel()));

                    //when all fields are from the same table ItemEntity
                    cb.and(cb.like(cb.lower(model), "%" + searchProductItemDTO.getModel().toLowerCase() + "%"))
            );
        }

        if (searchProductItemDTO.getMinPrice() != null) {
            predicate.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("sellingPrice"), searchProductItemDTO.getMinPrice()))
            );
        }

        if (searchProductItemDTO.getMaxPrice() != null) {
            predicate.getExpressions().add(
                    cb.and(cb.lessThanOrEqualTo(root.get("sellingPrice"), searchProductItemDTO.getMaxPrice()))
            );
        }

        return predicate;
    }
}
