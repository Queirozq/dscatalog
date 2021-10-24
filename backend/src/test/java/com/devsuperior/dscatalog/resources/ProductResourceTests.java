package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private PageImpl page;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp(){
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;



        /*Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);*/
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
        Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);

        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    /*@Test
    void findAllShouldReturnAPage() throws Exception{
         mockMvc.perform(MockMvcRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

         Mockito.verify(service).findAll(ArgumentMatchers.any());

    }*/

    @Test
    void findByIdShouldReturnAProductWhenIdExists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        Mockito.verify(service).findById(existingId);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception{
         mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", nonExistingId)).andExpect(status().isNotFound());

         Mockito.verify(service).findById(nonExistingId);
    }

    @Test
    void updateShouldReturnAProductWhenIdExists() throws Exception{
        String jsonResult = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                .content(jsonResult)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").exists());

    }

    @Test
    void updateShouldThrowNotFoundWhenIdDoesNotExists() throws Exception {

        String jsonResult = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                .content(jsonResult)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingId)).andExpect(status().isNoContent());

        Mockito.verify(service).delete(existingId);
    }

    @Test
    void deleteShouldThrowNotFoundWhenIdDoesNotExists() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", nonExistingId)).andExpect(status().isNotFound());

        Mockito.verify(service).delete(nonExistingId);
    }

    @Test
    void deleteShouldThrowBadRequestWhenIdIsDependent() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", dependentId)).andExpect(status().isBadRequest());

        Mockito.verify(service).delete(dependentId);
    }

    @Test
    void insertShouldReturnACreatedAndReturnAProductDTO() throws Exception{
        String jsonResult = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/products").content(jsonResult).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());

        Mockito.verify(service).insert(ArgumentMatchers.any());
    }
}
