package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Average Phone",800.00 ,"https://img.com//img.png", Instant.parse("2021-10-20T03:00:00Z"));
        product.getCategories().add(new Category(1L, "Eletronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory(){
        return new Category(1L, "Eletronics");
    }
}
