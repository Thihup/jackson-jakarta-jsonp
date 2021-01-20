package com.github.pgelinas.jackson.javax.json.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.github.pgelinas.jackson.javax.json.ConfigurationUtils;
import com.github.pgelinas.jackson.javax.json.JacksonValue;
import com.github.pgelinas.jackson.javax.json.NodeFactory;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.json.stream.JsonParserFactory;

public class JacksonParserFactory implements JsonParserFactory {
    private final JsonFactory _factory;
    private Map<String, Object> _configInUse;
    private final NodeFactory _nodeFactory;

    public JacksonParserFactory(JsonFactory factory, NodeFactory nodeFactory) {
        _factory = factory;
        _nodeFactory = nodeFactory;
    }

    public JacksonParserFactory(Map<String, ?> config, NodeFactory nodeFactory) {
        this(new JsonFactory(), nodeFactory);
        _configInUse = ConfigurationUtils.configure(_factory, config);
    }

    @Override
    public jakarta.json.stream.JsonParser createParser(Reader reader) {
        try {
            return new JacksonParser(_factory.createParser(reader));
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public jakarta.json.stream.JsonParser createParser(InputStream in) {
        try {
            return new JacksonParser(_factory.createParser(in));
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public jakarta.json.stream.JsonParser createParser(InputStream in, Charset charset) {
        try {
            return new JacksonParser(_factory.createParser(new InputStreamReader(in, charset)));
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public jakarta.json.stream.JsonParser createParser(JsonObject obj) {
        return parseTree(obj);
    }

    @Override
    public jakarta.json.stream.JsonParser createParser(JsonArray array) {
        return parseTree(array);
    }

    private jakarta.json.stream.JsonParser parseTree(JsonValue value) {
        JsonNode node;
        if (value instanceof JacksonValue) {
            node = ((JacksonValue<?>) value).delegate();
        } else {
            node = _nodeFactory.from(value);
        }

        return new JacksonParser(node.traverse());
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return _configInUse;
    }
}
