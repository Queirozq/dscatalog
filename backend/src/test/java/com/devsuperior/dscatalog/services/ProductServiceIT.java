package com.devsuperior.dscatalog.services;
import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp(){

        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;

    }

    @Test
    void deleteShouldDeleteResourceWhenIdExists(){

        service.delete(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    void deleteShouldThrownResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {service.delete(nonExistingId);});

    }

    @Test
    void findAllShouldReturnAPageWhenPage0Size10(){

        Pageable pageable = PageRequest.of(0,10);

        Page<ProductDTO> page = service.findAll(pageable, 0L, "");

        Assertions.assertTrue(page.hasContent());
        Assertions.assertEquals(0, page.getNumber());
        Assertions.assertEquals(10, page.getSize());
        Assertions.assertNotNull(page);
        Assertions.assertEquals(countTotalProducts, page.getTotalElements());
    }

    @Test
    void findAllShouldReturnAEmptyPageWhenPageDoesNotExists(){

        Pageable pageable = PageRequest.of(50,10);

        Page<ProductDTO> page = service.findAll(pageable, 0L, "");

        Assertions.assertTrue(page.isEmpty());
    }

    @Test
    void findAllShouldReturnASortedPage(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> page = service.findAll(pageable, 0L, "");

        Assertions.assertEquals("Macbook Pro", page.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", page.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", page.getContent().get(2).getName());
    }
}
