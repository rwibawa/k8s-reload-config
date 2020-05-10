package com.avisow.spring5;

import com.avisow.spring5.model.Employee;
import com.avisow.spring5.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeTests {

	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension();

	private WebTestClient webTestClient;

	@MockBean
	private EmployeeRepository employeeRepository;


	@BeforeEach
	public void setUp(ApplicationContext applicationContext,
					  RestDocumentationContextProvider restDocumentation) {
		this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
			.configureClient()
			.baseUrl("https://www.avisow.com")
			.filter(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	public void givenEmployeeId_whenGetEmployeeById_thenCorrectEmployee() {
		Employee employee = new Employee("1", "Employee 1");
		given(employeeRepository.findEmployeeById("1")).willReturn(Mono.just(employee));

		webTestClient.get()
				.uri("/employees/1")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Employee.class)
				.isEqualTo(employee)
				.consumeWith(document("employee"));
	}

	@Test
	public void whenGetAllEmployees_thenCorrectEmployees() {
		List<Employee> employees = Arrays.asList(
				new Employee("1", "Employee 1"),
				new Employee("2", "Employee 2"));

		Flux<Employee> employeeFlux = Flux.fromIterable(employees);
		given(employeeRepository.findAllEmployees()).willReturn(employeeFlux);

		webTestClient.get()
				.uri("/employees")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(Employee.class)
				.isEqualTo(employees)
				.consumeWith(document("employee"));
	}

	@Test
	public void whenUpdateEmployee_thenEmployeeUpdated() {
		Employee employee = new Employee("1", "Employee 1 Updated");

		webTestClient.post()
				.uri("/employees/update")
				.body(Mono.just(employee), Employee.class)
				.exchange()
				.expectStatus()
				.isOk();

		verify(employeeRepository).updateEmployee(employee);
	}
}
