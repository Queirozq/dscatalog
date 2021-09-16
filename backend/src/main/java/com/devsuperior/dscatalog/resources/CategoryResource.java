package com.devsuperior.dscatalog.resources;
import com.devsuperior.dscatalog.DTO.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;


     @GetMapping
     public ResponseEntity<Page<CategoryDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, @RequestParam(value = "orderBy", defaultValue = "name") String orderBy, @RequestParam(value = "direction", defaultValue = "ASC") String direction){
          PageRequest request = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
          Page<CategoryDTO> listDTO = service.findAll(request);
          return ResponseEntity.ok().body(listDTO);
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

     @DeleteMapping(value = "/{id}")
     public ResponseEntity<Void> delete(@PathVariable Long id){
         service.delete(id);
         return ResponseEntity.noContent().build();
     }
}
