package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable, Long categoryId, String name) {
        List<Category> cat = (categoryId == 0) ? null : List.of(categoryRepository.getOne(categoryId));
        Page<Product> products = repository.findAllCustom(pageable, cat, name);
        return products.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findByIdCustom(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO obj) {
        Product product = new Product();
        fromDTO(product, obj);
        repository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO obj) {
        try {
            Product product = repository.getOne(id);
            fromDTO(product,obj);
            product = repository.save(product);
            return new ProductDTO(product);
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
           throw new ResourceNotFoundException("Produto não encontrado");
       }
    }

    private void fromDTO(Product product, ProductDTO productDTO){

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImgUrl(productDTO.getImgUrl());
        product.setDate(productDTO.getDate());

        product.getCategories().clear();
        for(CategoryDTO catDTO : productDTO.getCategories()){
            Category category = categoryRepository.getOne(catDTO.getId());
            product.getCategories().add(category);
        }
    }
}
