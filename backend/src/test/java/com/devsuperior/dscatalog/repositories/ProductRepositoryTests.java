package com.devsuperior.dscatalog.repositories;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
       existingId = 1L;
       nonExistingId = 68L;
    }

    @Test
    void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {repository.deleteById(nonExistingId);});
    }

    @Test
    void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertEquals(26L, product.getId());
        Assertions.assertNotNull(product.getId());
    }

    @Test
    void findByIdShouldReturnOptionalNotEmptyWhenIdExist(){
        Optional<Product> product = repository.findById(existingId);

        Assertions.assertTrue(product.isPresent());
    }

    @Test
    void findByIdShouldReturnOptionalEmptyWhenIdDoesNotExists(){
        Optional<Product> product = repository.findById(nonExistingId);

        Assertions.assertTrue(product.isEmpty());
    }
}
