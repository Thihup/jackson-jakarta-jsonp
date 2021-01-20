
package com.github.pgelinas.jackson.javax.json;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

public class JacksonArray extends AbstractList<JsonValue> implements JsonArray, JacksonValue<ArrayNode> {
    private final NodeFactory _nodeFactory;
    private final ArrayNode _delegate;
    private List<JsonValue> _values;

    public JacksonArray(ArrayNode delegate, NodeFactory nodeFactory) {
        _delegate = delegate;
        _nodeFactory = nodeFactory;
    }

    @Override
    public int size() {
        return _delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return _delegate.size() == 0;
    }

    @Override
    public JsonObject getJsonObject(int index) {
        return (JsonObject) get(index);
    }

    @Override
    public JsonArray getJsonArray(int index) {
        return (JsonArray) get(index);
    }

    @Override
    public JsonNumber getJsonNumber(int index) {
        return (JsonNumber) get(index);
    }

    @Override
    public JsonString getJsonString(int index) {
        return (JsonString) get(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
        if (_values == null) {
            _values = new ArrayList<>();
            for (Iterator<JsonNode> iterator = _delegate.elements(); iterator.hasNext();) {
                _values.add(_nodeFactory.from(iterator.next()));
            }
            _values = Collections.unmodifiableList(_values);
        }
        return (List<T>) _values;
    }

    @Override
    public String getString(int index) {
        return getJsonString(index).getString();
    }

    @Override
    public String getString(int index, String defaultValue) {
        try {
            return getString(index);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    @Override
    public int getInt(int index) {
        return getJsonNumber(index).intValue();
    }

    @Override
    public int getInt(int index, int defaultValue) {
        try {
            return getInt(index);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(int index) {
        JsonValue value = get(index);
        if (value == JsonValue.TRUE)
            return true;
        if (value == JsonValue.FALSE)
            return false;
        throw new ClassCastException();
    }

    @Override
    public boolean getBoolean(int index, boolean defaultValue) {
        try {
            return getBoolean(index);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    @Override
    public boolean isNull(int index) {
        return getRaw(index).isNull();
    }

    @Override
    public ValueType getValueType() {
        return JacksonValueUtils.getValueType(this);
    }

    @Override
    public ArrayNode delegate() {
        return _delegate;
    }

    @Override
    public JsonValue get(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException();
        return _nodeFactory.from(getRaw(index));
    }
    
    private JsonNode getRaw(int index){
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException();
        return _delegate.get(index);
    }
    
    @Override
    public String toString() {
        return _delegate.toString();
    }

    @Override
    public boolean equals(Object o) {
        return JacksonValueUtils.isEquals(this, o);
    }
}
