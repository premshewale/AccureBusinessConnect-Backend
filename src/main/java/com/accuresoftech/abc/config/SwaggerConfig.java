package com.accuresoftech.abc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI().info(new Info().title("Accure Business Connect (ABC) CRM API").version("1.0.0")
				.description("Backend APIs for ABC CRM - Admin, Auth, Customer, and Lead modules")
				.license(new License().name("Apache 2.0")));
	}
}
