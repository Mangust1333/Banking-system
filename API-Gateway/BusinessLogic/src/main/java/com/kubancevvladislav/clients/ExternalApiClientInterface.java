package com.kubancevvladislav.clients;

import com.kubancevvladislav.domain.GetRequest;
import com.kubancevvladislav.domain.PostRequest;

import java.util.Optional;

public interface ExternalApiClientInterface {
    <T> Optional<T> get(GetRequest request, Class<T> responseType);

    <T> Optional<T> post(PostRequest request, Class<T> responseType);
}
