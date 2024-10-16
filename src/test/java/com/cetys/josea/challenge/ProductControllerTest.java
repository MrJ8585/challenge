package com.cetys.josea.challenge;

import com.cetys.josea.challenge.services.LLMService;
import com.cetys.josea.challenge.services.ProductService;
import com.cetys.josea.challenge.services.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private RedisService redisService;

    @Mock
    private ProductService productService;

    @Mock
    private LLMService llmService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProduct_ShouldReturnFromCacheWithStatusCreated() {
        String productId = "1";
        String cachedProduct = "cached product details";

        when(redisService.getValue(productId)).thenReturn(cachedProduct);

        ResponseEntity<Map<String, String>> response = productController.getProduct(productId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Map.of("product", cachedProduct), response.getBody());
        verify(redisService, times(1)).getValue(productId);
        verify(productService, times(0)).getProductById(anyInt());
        verify(llmService, times(0)).getFriendlyExplanation(anyString());
    }

    @Test
    void getProduct_ShouldReturnFromServiceWithStatusOk() {
        String productId = "1";
        String productJson = "{ \"id\": 1, \"name\": \"Test Product\" }";
        String friendlyExplanation = "This is a friendly explanation of the product.";

        when(redisService.getValue(productId)).thenReturn(null);
        when(productService.getProductById(Integer.parseInt(productId))).thenReturn(productJson);
        when(llmService.getFriendlyExplanation(productJson)).thenReturn(friendlyExplanation);

        ResponseEntity<Map<String, String>> response = productController.getProduct(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("product", friendlyExplanation), response.getBody());
        verify(redisService, times(1)).getValue(productId);
        verify(productService, times(1)).getProductById(Integer.parseInt(productId));
        verify(llmService, times(1)).getFriendlyExplanation(productJson);
        verify(redisService, times(1)).saveValue(productId, friendlyExplanation);
    }
}

