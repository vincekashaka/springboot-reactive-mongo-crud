package com.vince.reactive.service;

import com.vince.reactive.dto.ProductDto;
import com.vince.reactive.entity.Product;
import com.vince.reactive.repository.ProductRepository;
import com.vince.reactive.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;



    // Get API

    public Flux<ProductDto> getProducts(){
        return productRepository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id){
        return productRepository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductInRange(double min, double max){
        return productRepository.findByPriceBetween(Range.closed(min, max));
    }

    // POST API
    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
       return productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(productRepository::insert)
                .map(AppUtils::entityToDto);
    }

    //UPDATED
    public Mono<ProductDto> updatedProduct(Mono<ProductDto> productDtoMono, String id){
       return productRepository.findById(id)
                .flatMap(product -> productDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(e ->e.setId(id)))
                .flatMap(productRepository::save)
                .map(AppUtils::entityToDto);
    }
}
