package br.com.starwars.b2w.configurations;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class DocumentSwaggerConfiguration {

	@Bean
    public Docket apiDocket() {
		
		String groupName = "Swagger";
        
		return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.starwars.b2w"))
                .paths(PathSelectors.any())
                .build()
                .groupName(groupName)
                .apiInfo(metaInfo());
                
    }

    private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "B2W Company API RESTful - Star Wars.",
                "API REST para consulta de planetas Star Wars.",
                "2.0",
                "Terms of Service",
                new Contact("B2W StarWars API", "https://b2wstarwars.herokuapp.com/", "phillb2w@gmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0", 
                Collections.emptyList()
        );

        return apiInfo;
    }
    
}
