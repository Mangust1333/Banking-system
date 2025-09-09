package com.kubancevvladislav.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PostRequest {
    private String resource;
    private Object payload;
    private Map<String, String> headers;
}
