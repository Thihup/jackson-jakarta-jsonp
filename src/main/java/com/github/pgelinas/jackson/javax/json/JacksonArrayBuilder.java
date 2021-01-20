package com.github.pgelinas.jackson.javax.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

public class JacksonArrayBuilder implements JsonArrayBuilder {
    private final JsonNodeFactory _jsonNodeFactory;
    private final NodeFactory _nodeFactory;
    private final ArrayNode _delegate;

    public JacksonArrayBuilder(JsonNodeFactory jsonNodeFactory, NodeFactory nodeFactory) {
        _jsonNodeFactory = jsonNodeFactory;
        _nodeFactory = nodeFactory;
        _delegate = jsonNodeFactory.arrayNode();
    }

    public ArrayNode delegate() {
        return _delegate;
    }

    @Override
    public JsonArrayBuilder add(JsonValue value) {
        Objects.requireNonNull(value);
        _delegate.add(_nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder add(String value) {
        Objects.requireNonNull(value);
        _delegate.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(BigDecimal value) {
        Objects.requireNonNull(value);
        _delegate.add(_nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder add(BigInteger value) {
        Objects.requireNonNull(value);
        _delegate.add(_nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int value) {
        _delegate.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(long value) {
        _delegate.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new NumberFormatException();
        }
        _delegate.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(boolean value) {
        _delegate.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder addNull() {
        _delegate.addNull();
        return this;
    }

    @Override
    public JsonArrayBuilder add(JsonObjectBuilder builder) {
        if (builder == null) throw new NullPointerException();
        if (!(builder instanceof JacksonObjectBuilder)) {
            _delegate.add(_nodeFactory.from(builder.build()));
        } else {
            _delegate.add(((JacksonObjectBuilder) builder).delegate());
        }
        return this;
    }

    @Override
    public JsonArrayBuilder add(JsonArrayBuilder builder) {
        if (builder == null) throw new NullPointerException();
        if (!(builder instanceof JacksonArrayBuilder)) {
            _delegate.add(_nodeFactory.from(builder.build()));
        } else {
            _delegate.add(((JacksonArrayBuilder) builder).delegate());
        }
        return this;
    }

    @Override
    public JsonArray build() {
        return new JacksonArray(_delegate.deepCopy(), _nodeFactory);
    }

    @Override
    public JsonArrayBuilder addAll(JsonArrayBuilder builder) {
        builder.build().forEach(this::add);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonValue value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.insert(index, _nodeFactory.from(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, String value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.insert(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, BigDecimal value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.insert(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, BigInteger value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.insert(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, int value) {
        outOfBounds(index);
        _delegate.insert(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, long value) {
        outOfBounds(index);
        _delegate.insert(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, double value) {
        outOfBounds(index);
        _delegate.insert(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, boolean value) {
        outOfBounds(index);
        _delegate.insert(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder addNull(int index) {
        outOfBounds(index);
        _delegate.insert(index, (JsonNode) null);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonObjectBuilder builder) {
        Objects.requireNonNull(builder);
        outOfBounds(index);
        if (!(builder instanceof JacksonObjectBuilder)) {
            _delegate.insert(index, _nodeFactory.from(builder.build()));
        } else {
            _delegate.insert(index, ((JacksonObjectBuilder) builder).delegate());
        }
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonArrayBuilder builder) {
        Objects.requireNonNull(builder);
        outOfBounds(index);
        if (!(builder instanceof JacksonArrayBuilder)) {
            _delegate.insert(index, _nodeFactory.from(builder.build()));
        } else {
            _delegate.insert(index, ((JacksonArrayBuilder) builder).delegate());
        }
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonValue value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, String value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, BigDecimal value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, BigInteger value) {
        Objects.requireNonNull(value);
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, int value) {
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, long value) {
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, double value) {
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, boolean value) {
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(_nodeFactory.toJsonValue(value)));
        return this;
    }

    @Override
    public JsonArrayBuilder setNull(int index) {
        outOfBounds(index);
        _delegate.set(index, _nodeFactory.from(JsonValue.NULL));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonObjectBuilder builder) {
        Objects.requireNonNull(builder);
        outOfBounds(index);
        if (!(builder instanceof JacksonObjectBuilder)) {
            _delegate.set(index, _nodeFactory.from(builder.build()));
        } else {
            _delegate.set(index, ((JacksonObjectBuilder) builder).delegate());
        }
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonArrayBuilder builder) {
        Objects.requireNonNull(builder);
        outOfBounds(index);
        if (!(builder instanceof JacksonArrayBuilder)) {
            _delegate.set(index, _nodeFactory.from(builder.build()));
        } else {
            _delegate.set(index, ((JacksonArrayBuilder) builder).delegate());
        }
        return this;
    }

    @Override
    public JsonArrayBuilder remove(int index) {
        outOfBounds(index);
        _delegate.remove(index);
        return this;
    }

    private void outOfBounds(int index){
        if (index < 0 || index > _delegate.size())
            throw new IndexOutOfBoundsException();
    }
}
