package com.devsuperior.dscatalog.resources;
import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private ProductDTO productDTO;

    private String username;
    private String password;

    @BeforeEach
    void setUp(){

        productDTO = Factory.createProductDTO();
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        username = "maria@gmail.com";
        password = "123456";

    }

    @Test
    void findAllShouldReturnSortedPageWhenSortByName() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/products?sort=name")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(countTotalProducts))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].name").value("Macbook Pro"))
                .andExpect(jsonPath("$.content[1].name").value("PC Gamer"))
                .andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception{

        String jsonResult = objectMapper.writeValueAsString(productDTO);

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId).header("Authorization", "Bearer" + accessToken)
                        .content(jsonResult).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(existingId))
                        .andExpect(jsonPath("$.name").value("Phone"));
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{

        String jsonResult = objectMapper.writeValueAsString(productDTO);

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonResult)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
}
