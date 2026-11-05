package br.com.seryoustech.standard.service;

import br.com.seryoustech.shared.exception.BusinessException;
import br.com.seryoustech.shared.exception.ResourceNotFoundException;
import br.com.seryoustech.standard.dto.ProductRequestDTO;
import br.com.seryoustech.standard.dto.ProductResponseDTO;
import br.com.seryoustech.standard.mapper.ProductMapper;
import br.com.seryoustech.standard.model.Product;
import br.com.seryoustech.standard.repository.ProductRepository;
import br.com.seryoustech.standard.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Teclado Mecânico", "Teclado switch brown", new BigDecimal("350.00"), 10, true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        requestDTO = new ProductRequestDTO(
                "Teclado Mecânico",
                "Teclado switch brown",
                new BigDecimal("350.00"),
                10,
                true
        );
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso quando o nome for único")
    void shouldCreateProductSuccessfully() {
        when(productRepository.existsByNameIgnoreCase(requestDTO.name())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.create(requestDTO);

        assertNotNull(response);
        assertEquals(product.getId(), response.id());
        assertEquals(product.getName(), response.name());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao cadastrar produto com nome duplicado")
    void shouldThrowBusinessExceptionWhenProductNameAlreadyExists() {
        when(productRepository.existsByNameIgnoreCase(requestDTO.name())).thenReturn(true);

        assertThrows(BusinessException.class, () -> productService.create(requestDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve retornar produto quando ID existir")
    void shouldReturnProductWhenIdExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Teclado Mecânico", response.name());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando ID não existir")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(99L));
    }

    @Test
    @DisplayName("Deve listar todos os produtos ativos")
    void shouldReturnAllActiveProducts() {
        when(productRepository.findByActiveTrue()).thenReturn(List.of(product));

        List<ProductResponseDTO> response = productService.findAllActive();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(product.getName(), response.get(0).name());
    }

    @Test
    @DisplayName("Deve inativar produto com sucesso")
    void shouldInactivateProductSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.updateStatus(1L, false);

        assertNotNull(response);
        assertFalse(response.active());
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Deve excluir produto quando ID existir")
    void shouldDeleteProductWhenIdExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository).delete(product);
    }
}
