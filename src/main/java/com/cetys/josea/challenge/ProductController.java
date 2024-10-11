package com.cetys.josea.challenge;

import com.cetys.josea.challenge.services.LLMService;
import com.cetys.josea.challenge.services.ProductService;
import com.cetys.josea.challenge.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ProductController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Autowired
    private LLMService llmService;

    @GetMapping("/products/{id}")
    public ResponseEntity<String> getProduct(@PathVariable String id) {
        // Buscar en Redis si el ID existe
        String cachedProduct = redisService.getValue(id);

        if (cachedProduct != null) {
            // Si existe, devolver el JSON guardado con el estado 201
            return ResponseEntity.status(HttpStatus.CREATED).body(cachedProduct);
        }

        // Si no existe, llamar al ProductService para obtener el producto
        String productJson = productService.getProductById(Integer.parseInt(id));

        // Llamar a LLMService para explicar el producto
        String llmResult = llmService.explainProduct(productJson);

        // Guardar el resultado en Redis
        redisService.saveValue(id, llmResult);

        // Devolver el resultado con un estado 200
        return ResponseEntity.status(HttpStatus.OK).body(llmResult);
    }
}
