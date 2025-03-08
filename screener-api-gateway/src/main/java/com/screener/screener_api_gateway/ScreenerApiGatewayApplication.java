package com.screener.screener_api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
public class ScreenerApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreenerApiGatewayApplication.class, args);
	}

}
