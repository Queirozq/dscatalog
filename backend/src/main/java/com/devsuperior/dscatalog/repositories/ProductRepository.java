package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id = :id ")
    Optional<Product> findByIdCustom(Long id);

    @Query("SELECT DISTINCT obj from Product obj INNER JOIN obj.categories cats WHERE " +
            "(COALESCE(:categories) IS NULL OR cats IN :categories) AND " +
            "(LOWER (obj.name) LIKE LOWER (CONCAT('%',:name, '%')))")
    Page<Product> findAllCustom(Pageable pageable, List<Category> categories, String name);

}
