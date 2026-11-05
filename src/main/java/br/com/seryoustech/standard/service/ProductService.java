package br.com.seryoustech.standard.service;

import br.com.seryoustech.standard.dto.ProductRequestDTO;
import br.com.seryoustech.standard.dto.ProductResponseDTO;

import java.util.List;

/**
 * Interface de contrato do serviço de Produtos (REST Padrão).
 */
public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO requestDTO);

    List<ProductResponseDTO> findAll();

    List<ProductResponseDTO> findAllActive();

    ProductResponseDTO findById(Long id);

    ProductResponseDTO update(Long id, ProductRequestDTO requestDTO);

    ProductResponseDTO updateStatus(Long id, boolean active);

    void delete(Long id);
}
