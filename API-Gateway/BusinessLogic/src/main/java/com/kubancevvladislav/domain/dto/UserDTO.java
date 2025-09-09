package com.kubancevvladislav.domain.dto;

import java.util.List;

public record UserDTO(String name,
                      String login,
                      short age,
                      GenderDTO gender,
                      HairColorDTO hairColor,
                      List<String> friends
) {}
