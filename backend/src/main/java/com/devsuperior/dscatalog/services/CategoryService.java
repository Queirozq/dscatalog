package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(PageRequest request) {
        Page<Category> categories = repository.findAll(request);
        return categories.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO obj) {
        Category category = new Category();
        category.setName(obj.getName());
        category = repository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO obj) {
        try {
            Category category = repository.getOne(id);
            category.setName(obj.getName());
            category = repository.save(category);
            return new CategoryDTO(category);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
    }

    public void delete(Long id){
       try{
           repository.deleteById(id);
       } catch(DataIntegrityViolationException e){
           throw new DatabaseException("Não é possível deletar uma categoria que tem produtos");
       }
       catch(EmptyResultDataAccessException e){
           throw new ResourceNotFoundException("Categoria não encontrada");
       }
    }
}
