package com.vince.reactive.controller;

import com.vince.reactive.dto.ProductDto;
import com.vince.reactive.entity.Product;
import com.vince.reactive.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Flux<ProductDto> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable String id){
        return productService.getProductById(id);
    }

    @GetMapping("/{product-range}")
    public Flux<ProductDto> getProductBetweenRange(@RequestParam("min") double min, @RequestParam("max")double max){
        return productService.getProductInRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return productService.saveProduct(productDtoMono);
    }

    @PutMapping("/update/{id}")
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable String id){
        return productService.updatedProduct(productDtoMono, id);
    }


    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return productService.deleteProduct(id);
    }
}
