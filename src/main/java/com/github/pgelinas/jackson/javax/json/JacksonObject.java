package com.github.pgelinas.jackson.javax.json;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

/**
 * JsonObject extends the Map interface, so let's have some fun...
 */
public class JacksonObject extends AbstractMap<String, JsonValue> implements JsonObject, JacksonValue<ObjectNode> {
    private final NodeFactory _nodeFactory;
    private final ObjectNode _delegate;
    
    private Set<Entry<String, JsonValue>> _entries;

    public JacksonObject(ObjectNode delegate, NodeFactory nodeFactory) {
        _delegate = delegate;
        _nodeFactory = nodeFactory;
    }

    @Override
    public Set<Map.Entry<String, JsonValue>> entrySet() {
        if (_entries == null) {
            _entries = new HashSet<>();
            for (Iterator<Entry<String, JsonNode>> iterator = _delegate.fields(); iterator.hasNext();) {
                _entries.add(new JacksonEntry(iterator.next(), _nodeFactory));
            }
            _entries = Collections.unmodifiableSet(_entries);
        }
        return _entries;
    }

    @Override
    public JsonArray getJsonArray(String name) {
        return (JsonArray) get(name);
    }

    @Override
    public JsonObject getJsonObject(String name) {
        return (JsonObject) get(name);
    }

    @Override
    public JsonNumber getJsonNumber(String name) {
        return (JsonNumber) get(name);
    }

    @Override
    public JsonString getJsonString(String name) {
        return (JsonString) get(name);
    }

    @Override
    public String getString(String name) {
        return _delegate.get(name).asText();
    }

    @Override
    public String getString(String name, String defaultValue) {
        JsonNode jsonNode = _delegate.get(name);
        return (jsonNode == null) ? defaultValue : jsonNode.asText();
    }

    @Override
    public int getInt(String name) {
        return _delegate.get(name).asInt();
    }

    @Override
    public int getInt(String name, int defaultValue) {
        JsonNode jsonNode = _delegate.get(name);
        return (jsonNode == null) ? defaultValue : jsonNode.asInt(defaultValue);
    }

    @Override
    public boolean getBoolean(String name) {
        return _delegate.get(name).asBoolean();
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        JsonNode jsonNode = _delegate.get(name);
        return (jsonNode == null) ? defaultValue : jsonNode.asBoolean(defaultValue);
    }

    @Override
    public boolean isNull(String name) {
        return _delegate.get(name).isNull();
    }

    @Override
    public ValueType getValueType() {
        return JacksonValueUtils.getValueType(this);
    }

    @Override
    public ObjectNode delegate() {
        return _delegate;
    }
    
    @Override
    public String toString() {
        return _delegate.toString();
    }
}
