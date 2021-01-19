package com.github.pgelinas.jackson.javax.json;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.json.JsonValue;

public interface JacksonValue<T extends JsonNode> extends JsonValue {
    T delegate();
}
