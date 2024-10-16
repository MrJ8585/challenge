package com.cetys.josea.challenge;
import com.cetys.josea.challenge.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductById_ShouldReturnProductJson() {
        int productId = 1;
        String expectedResponse = "{ \"id\": 1, \"name\": \"Test Product\" }";
        String url = "https://dummyjson.com/products/" + productId;

        when(restTemplate.getForObject(url, String.class)).thenReturn(expectedResponse);

        String actualResponse = productService.getProductById(productId);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate, times(1)).getForObject(url, String.class);
    }
}
