package com.cetys.josea.challenge;

import com.cetys.josea.challenge.services.LLMService;
import com.cetys.josea.challenge.services.ProductService;
import com.cetys.josea.challenge.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestController
@ResponseBody
public class ProductController {

    private final RedisService redisService;
    private final ProductService productService;
    private final LLMService llmService;

    @Autowired
    public ProductController(RedisService redisService, ProductService productService, LLMService llmService) {
        this.redisService = redisService;
        this.productService = productService;
        this.llmService = llmService;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Map<String, String>> getProduct(@PathVariable String id) {
        // Verificar si el producto ya está en caché
        String cachedProduct = redisService.getValue(id);

        if (cachedProduct != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("product", cachedProduct));
        }

        // Si no está en caché, obtener el producto desde el ProductService
        String productJson = productService.getProductById(Integer.parseInt(id));

        // Obtener la explicación amigable desde LLMService
        String response = llmService.getFriendlyExplanation(productJson);

        // Guardar la respuesta en Redis
        redisService.saveValue(id, response);

        // Retornar la explicación generada
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("product", response));
    }
}