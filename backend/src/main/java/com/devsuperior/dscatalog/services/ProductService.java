package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(PageRequest request) {
        Page<Product> products = repository.findAll(request);
        return products.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO obj) {
        Product Product = new Product();
        //Product.setName(obj.getName());
        Product = repository.save(Product);
        return new ProductDTO(Product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO obj) {
        try {
            Product Product = repository.getOne(id);
            //Product.setName(obj.getName());
            Product = repository.save(Product);
            return new ProductDTO(Product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }
    }

    public void delete(Long id){
       try{
           repository.deleteById(id);
       } catch(DataIntegrityViolationException e){
           throw new DatabaseException("Não é possível deletar esse produto");
       }
       catch(EmptyResultDataAccessException e){
           throw new ResourceNotFoundException("Produto não encontrada");
       }
    }
}
