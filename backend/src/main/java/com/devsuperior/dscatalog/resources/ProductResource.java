package com.devsuperior.dscatalog.resources;
import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService service;


     @GetMapping
     public ResponseEntity<Page<ProductDTO>> findAll(@RequestParam(value = "categoryId", defaultValue = "0")Long categoryId,
                                                     @RequestParam(value = "name", defaultValue = "")String name,
                                                     Pageable pageable){
          Page<ProductDTO> listDTO = service.findAll(pageable, categoryId, name.trim());
          return ResponseEntity.ok().body(listDTO);
     }

     @GetMapping(value = "/{id}")
     public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
     }

     @PostMapping
     public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO obj){
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
     }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO obj){
        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
         service.delete(id);
         return ResponseEntity.noContent().build();
    }
}
