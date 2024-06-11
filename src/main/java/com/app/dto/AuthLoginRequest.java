package com.app.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NonNull;

@JsonPropertyOrder({"username", "password"})
public record AuthLoginRequest(@NonNull String username,@NonNull String password) {
}
