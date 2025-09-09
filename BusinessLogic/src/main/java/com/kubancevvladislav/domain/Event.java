package com.kubancevvladislav.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class Event {
    String eventName;
    String eventDescription;
    List<Object> eventData;
}
