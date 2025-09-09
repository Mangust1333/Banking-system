package com.kubancevvladislav.domain.dto;

import java.math.BigInteger;
import java.util.List;

public record FriendInfoDTO(String name, List<BigInteger> accountIds) {}


