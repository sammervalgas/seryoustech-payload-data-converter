package br.com.seryoustech.standard.controller;

import br.com.seryoustech.standard.dto.ProductRequestDTO;
import br.com.seryoustech.standard.dto.ProductResponseDTO;
import br.com.seryoustech.standard.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller da arquitetura REST Padrão.
 * Implementa o contrato ProductMAPI.
 */
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductMAPI {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductResponseDTO> create(ProductRequestDTO requestDTO) {
        ProductResponseDTO createdProduct = productService.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.id())
                .toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    @Override
    public ResponseEntity<List<ProductResponseDTO>> findAll(boolean activeOnly) {
        List<ProductResponseDTO> list = activeOnly ? productService.findAllActive() : productService.findAll();
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<ProductResponseDTO> findById(Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Override
    public ResponseEntity<ProductResponseDTO> update(Long id, ProductRequestDTO requestDTO) {
        return ResponseEntity.ok(productService.update(id, requestDTO));
    }

    @Override
    public ResponseEntity<ProductResponseDTO> updateStatus(Long id, boolean active) {
        return ResponseEntity.ok(productService.updateStatus(id, active));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
