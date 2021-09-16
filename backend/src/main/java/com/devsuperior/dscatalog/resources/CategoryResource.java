package com.devsuperior.dscatalog.resources;
import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;


     @GetMapping
     public ResponseEntity<List<CategoryDTO>> findAll(){
          List<CategoryDTO> list = service.findAll();
          return ResponseEntity.ok().body(list);
     }

     @GetMapping(value = "/{id}")
     public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
     }

     @PostMapping
     public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO obj){
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
     }

     @PutMapping(value = "/{id}")
     public ResponseEntity<CategoryDTO> update(@PathVariable Long id,@RequestBody CategoryDTO obj){
        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);
     }
}
