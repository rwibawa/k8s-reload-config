package com.avisow.spring5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith(SpringExtension.class)
//  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Spring5ApplicationTests {

	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension ("custom");

	private WebTestClient webTestClient;

	@BeforeEach
	public void setUp(ApplicationContext applicationContext,
					  RestDocumentationContextProvider restDocumentation) {
		this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
				.configureClient()
				.filter(documentationConfiguration(restDocumentation))
				.build();
	}

	@Test
	public void testHomepage() {
		webTestClient
				.get().uri("/")
				.accept(MediaType.TEXT_PLAIN)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("Home page");
	}

	@Test
	public void testHello() {
		webTestClient
			// Create a GET request to test an endpoint
			.get().uri("/hello")
			.accept(MediaType.TEXT_PLAIN)
			.exchange()
			// and use the dedicated DSL to test assertions against the response
			.expectStatus().isOk()
			.expectBody(String.class).isEqualTo("Hello, Spring!");
	}

}
