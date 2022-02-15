package com.doctorvee.multiplicationapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class Swag {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.doctorvee.multiplicationapp"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo(){
        return new ApiInfo(
                "Multiplication App",
                "An API Service that generates multiplications questions and options with indication of the correct option",
                "1.0",
                "No terms of use ... it's free",
                new Contact("Doctor Vee", "https://github.com/Victor-Chinewubeze", "ovisco360@gmail.com"),
                "No need for license biko... Click this to see me on LinkedIn",
                "https://www.linkedin.com/in/victor-chinewubeze/",
                Collections.emptyList()
        );
    }
}
