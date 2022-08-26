package com.vince.reactive;

import com.vince.reactive.controller.ProductController;
import com.vince.reactive.dto.ProductDto;
import com.vince.reactive.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringReactiveMongoCrudApplicationTests {

	@Autowired
	 private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;


	@Test
	public void addProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("111","dish", 4, 350 ));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		webTestClient.post().uri("/api/v1/products")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void getProduct(){
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("111","dish", 4, 350 ),
				new ProductDto("101","microwave", 3, 700 ));
		when(productService.getProducts()).thenReturn(productDtoFlux);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/api/v1/products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("101","microwave", 3, 700 ))
				.expectNext(new ProductDto("111","dish", 4, 350 ))
				.verifyComplete();


	}

}
