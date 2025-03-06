package com.screener.user_service_backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "User Service Backend",
		version = "v1",
		description = "User Service Backend",
			license = @License(
				name = "Apache 2.0",
				url = "https://www.apache.org/licenses/LICENSE-2.0"
			)
	)
)
@EnableDiscoveryClient
public class UserServiceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceBackendApplication.class, args);
	}

}
