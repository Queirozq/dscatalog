package com.devsuperior.dscatalog.services;
import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        category = Factory.createCategory();
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));


        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.findByIdCustom(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findByIdCustom(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(repository.findAllCustom1(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(page);


        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }

    @Test
    void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {service.delete(existingId);});

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {service.delete(nonExistingId);});

        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    void deleteShouldThrownDatabaseExceptionWhenIdIsDependent(){

        Assertions.assertThrows(DatabaseException.class,(() -> {service.delete(dependentId);}));

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    void findAllPagedShouldReturnAPage(){

        Pageable pageable = PageRequest.of(2,2);

        Page<ProductDTO> page1 = service.findAll(pageable, 0L, "");

        Assertions.assertTrue(page1.hasContent());
        Assertions.assertNotNull(page1);
    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists(){

        ProductDTO productDTO = service.findById(existingId);

        Assertions.assertNotNull(productDTO);
        Mockito.verify(repository).findByIdCustom(existingId);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> {service.findById(nonExistingId);});
        Mockito.verify(repository).findByIdCustom(nonExistingId);
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = service.update(existingId, productDTO);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).save(ArgumentMatchers.any());
        Mockito.verify(repository).getOne(existingId);
        Mockito.verify(categoryRepository).getOne(ArgumentMatchers.any());

    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {service.update(nonExistingId, productDTO);});
        Mockito.verify(repository).getOne(nonExistingId);
    }
}
