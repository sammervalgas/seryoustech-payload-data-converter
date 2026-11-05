package br.com.seryoustech.standard.mapper;

import br.com.seryoustech.standard.dto.ProductRequestDTO;
import br.com.seryoustech.standard.dto.ProductResponseDTO;
import br.com.seryoustech.standard.model.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão e sincronização de dados entre DTOs e a Entidade Product.
 */
@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setActive(dto.active() != null ? dto.active() : true);
        return product;
    }

    public ProductResponseDTO toResponseDTO(Product entity) {
        if (entity == null) {
            return null;
        }

        return new ProductResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public void updateEntityFromDTO(Product entity, ProductRequestDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setStockQuantity(dto.stockQuantity());
        if (dto.active() != null) {
            entity.setActive(dto.active());
        }
    }
}
