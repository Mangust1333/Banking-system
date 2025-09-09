package com.kubancevvladislav.utils;

import com.kubancevvladislav.clients.ExternalApiClientInterface;
import com.kubancevvladislav.domain.GetRequest;
import com.kubancevvladislav.domain.PostRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@AllArgsConstructor
public class HttpExternalApiClient implements ExternalApiClientInterface {
    private final RestTemplate restTemplate;

    @Override
    public <T> Optional<T> get(GetRequest request, Class<T> responseType) {
        String url = request.getResource();

        HttpHeaders headers = new HttpHeaders();
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(headers::set);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return Optional.ofNullable(response.getBody());
    }

    @Override
    public <T> Optional<T> post(PostRequest request, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(headers::set);
        }

        HttpMethod method = HttpMethod.POST;

        HttpEntity<Object> entity = new HttpEntity<>(request.getPayload(), headers);
        ResponseEntity<T> response = restTemplate.exchange(request.getResource(), method, entity, responseType);
        return Optional.ofNullable(response.getBody());
    }
}
