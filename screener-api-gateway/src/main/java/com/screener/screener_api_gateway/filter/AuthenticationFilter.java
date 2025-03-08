package com.screener.screener_api_gateway.filter;

import com.screener.screener_api_gateway.errors.CustomResponseStatusException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
                return webClient.post()
                        .uri("lb://USER-SERVICE-BACKEND/api/v1/auth/validate-token?token={token}", token)
                        .retrieve()
                        .toBodilessEntity()
                        .then(chain.filter(exchange))
                        .onErrorResume(this::handleError);
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> handleError(Throwable ex) {
        if (ex instanceof WebClientResponseException webEx) {
            HttpStatus status = (HttpStatus) webEx.getStatusCode();
            String message = "Invalid or expired token, please refresh token to continue";
            return Mono.error(new CustomResponseStatusException(status, message));
        }
        log.error("Service unavailable: {}", ex.getMessage());
        return Mono.error(new CustomResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Authentication service unavailable"));
    }


    public static class Config {}
}
