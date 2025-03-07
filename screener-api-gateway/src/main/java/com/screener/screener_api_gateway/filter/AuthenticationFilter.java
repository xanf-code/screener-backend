package com.screener.screener_api_gateway.filter;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;

    private final RestTemplate restTemplate;

    public AuthenticationFilter(RouteValidator validator, RestTemplate restTemplate) {
        super(Config.class);
        this.validator = validator;
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                String url = "http://USER-SERVICE-BACKEND/api/v1/auth/validate-token?token=" + authHeader;
                try {
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
                } catch (HttpClientErrorException ex) {
                    if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
                    } else {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to application");
                    }
                } catch (Exception ex) {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                            "Authentication service unavailable: " + ex.getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {}
}
