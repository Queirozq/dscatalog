package com.devsuperior.dscatalog.DTO;

import com.devsuperior.dscatalog.entities.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryDTO implements Serializable {

    private Long id;
    private String name;

    private List<ProductDTO> products = new ArrayList<>();

    public CategoryDTO() {
    }

    public CategoryDTO(Category obj) {
        this.id = obj.getId();
        this.name = obj.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
