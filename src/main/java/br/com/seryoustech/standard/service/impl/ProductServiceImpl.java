package br.com.seryoustech.standard.service.impl;

import br.com.seryoustech.shared.exception.BusinessException;
import br.com.seryoustech.shared.exception.ResourceNotFoundException;
import br.com.seryoustech.standard.dto.ProductRequestDTO;
import br.com.seryoustech.standard.dto.ProductResponseDTO;
import br.com.seryoustech.standard.mapper.ProductMapper;
import br.com.seryoustech.standard.model.Product;
import br.com.seryoustech.standard.repository.ProductRepository;
import br.com.seryoustech.standard.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação das regras de negócio do CRUD tradicional em camadas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDTO create(ProductRequestDTO requestDTO) {
        log.info("[REST Padrão] Cadastrando novo produto: {}", requestDTO.name());

        if (productRepository.existsByNameIgnoreCase(requestDTO.name())) {
            throw new BusinessException(String.format("Já existe um produto cadastrado com o nome '%s'", requestDTO.name()));
        }

        Product product = productMapper.toEntity(requestDTO);
        Product savedProduct = productRepository.save(product);

        log.info("[REST Padrão] Produto ID {} cadastrado com sucesso", savedProduct.getId());
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> findAll() {
        log.debug("[REST Padrão] Listando todos os produtos");
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> findAllActive() {
        log.debug("[REST Padrão] Listando produtos ativos");
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        log.debug("[REST Padrão] Buscando produto ID: {}", id);
        Product product = findEntityById(id);
        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO requestDTO) {
        log.info("[REST Padrão] Atualizando produto ID: {}", id);

        Product existingProduct = findEntityById(id);

        if (productRepository.existsByNameIgnoreCaseAndIdNot(requestDTO.name(), id)) {
            throw new BusinessException(String.format("Já existe outro produto cadastrado com o nome '%s'", requestDTO.name()));
        }

        productMapper.updateEntityFromDTO(existingProduct, requestDTO);
        Product updatedProduct = productRepository.save(existingProduct);

        log.info("[REST Padrão] Produto ID {} atualizado com sucesso", id);
        return productMapper.toResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateStatus(Long id, boolean active) {
        log.info("[REST Padrão] Alterando status do produto ID: {} para ativo={}", id, active);

        Product product = findEntityById(id);
        product.setActive(active);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("[REST Padrão] Excluindo produto ID: {}", id);

        Product product = findEntityById(id);
        productRepository.delete(product);

        log.info("[REST Padrão] Produto ID {} excluído com sucesso", id);
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
    }
}
