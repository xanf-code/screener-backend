package com.screener.screener_api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/verify-user",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/update-password",
            "/api/v1/auth/validate-token",
            "/eureka"
    );

    // TODO: Logic for check is too permissive, try implementing regex later on
    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
            .stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
