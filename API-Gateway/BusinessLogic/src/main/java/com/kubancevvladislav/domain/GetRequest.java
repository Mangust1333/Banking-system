package com.kubancevvladislav.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class GetRequest {
    private String resource;
    private Map<String, String> headers;
}
