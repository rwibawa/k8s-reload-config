package com.avisow.spring5.config;

import com.avisow.spring5.model.Employee;
import com.avisow.spring5.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class EmployeeRouter {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRouter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public RouterFunction<ServerResponse> employeeRoutes() {
        return
            route(GET("/employees"),
                req -> ok().body(
                    employeeRepository.findAllEmployees(), Employee.class))

                .and(route(GET("/employees/{id}"),
                    req -> ok().body(
                    employeeRepository.findEmployeeById(req.pathVariable("id")), Employee.class)))

                .and(route(POST("/employees/update"),
                    req -> req.body(toMono(Employee.class))
                            .doOnNext(employeeRepository::updateEmployee)
                            .then(ok().build())));
    }
}
