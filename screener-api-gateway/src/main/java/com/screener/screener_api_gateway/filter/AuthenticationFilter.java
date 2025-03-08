package com.screener.screener_api_gateway.filter;

import com.screener.screener_api_gateway.dto.response.GenericResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;

    private final WebClient webClient;

    public AuthenticationFilter(RouteValidator validator, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.validator = validator;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header"));
                }

                String token = authHeader.substring(7);
                log.info("Validating token: {}", token);

                return webClient.post()
                        .uri("lb://USER-SERVICE-BACKEND/validate-token?token={token}", token)
                        .retrieve()
                        .toBodilessEntity()
                        .then(chain.filter(exchange))
                        .onErrorResume(this::handleError);
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> handleError(Throwable ex) {
        if (ex instanceof HttpClientErrorException) {
            HttpStatus status = (HttpStatus) ((HttpClientErrorException) ex).getStatusCode();
            String message = (status == HttpStatus.BAD_REQUEST) ? "Invalid token" : "Unauthorized access";
            return Mono.error(new ResponseStatusException(status, message));
        }
        log.error("Service unavailable: {}", ex.getMessage());
        return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Authentication service unavailable"));
    }

    public static class Config {}
}
