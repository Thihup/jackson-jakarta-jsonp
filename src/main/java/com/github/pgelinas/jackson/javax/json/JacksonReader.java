package com.github.pgelinas.jackson.javax.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.pgelinas.jackson.javax.json.stream.JacksonLocation;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

import jakarta.json.stream.JsonParsingException;

public class JacksonReader implements JsonReader {
    private final ObjectMapper _mapper;
    private final NodeFactory _nodeFactory;
    private Reader _reader;
    private InputStream _in;
    private boolean _closed;

    public JacksonReader(ObjectMapper mapper, NodeFactory nodeFactory, Reader reader) {
        _mapper = mapper;
        _nodeFactory = nodeFactory;
        _reader = reader;
    }

    public JacksonReader(ObjectMapper mapper, NodeFactory nodeFactory, InputStream in) {
        _mapper = mapper;
        _nodeFactory = nodeFactory;
        _in = in;
    }

    @Override
    public JsonStructure read() {
        return (JsonStructure) read(ContainerNode.class);
    }

    @Override
    public JsonObject readObject() {
        return (JsonObject) read(ObjectNode.class);
    }

    @Override
    public JsonArray readArray() {
        return (JsonArray) read(ArrayNode.class);
    }

    @Override
    public JsonValue readValue() {
        return read(ValueNode.class);
    }

    private <T extends JsonNode> JsonValue read(Class<T> type) {
        if(_closed) throw new IllegalStateException();
        T node;
        try {
            if (_reader != null) {
                node = _mapper.readValue(_reader, type);
            } else {
                node = _mapper.readValue(_in, type);
            }
            _closed = true;
        } catch (JsonProcessingException exception) {
            throw new JsonParsingException("", new JacksonLocation(exception.getLocation()));
        } catch (IOException exception) {
            throw new JsonException("", exception);
        }
        return _nodeFactory.from(node);
    }

    @Override
    public void close() {
        try {
            if (_reader != null) {
                _reader.close();
            } else {
                _in.close();
            }
            _closed =  true;
        } catch (IOException exception) {
            throw new JsonException("", exception);
        }
    }
}
