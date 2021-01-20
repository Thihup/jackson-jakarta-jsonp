package com.github.pgelinas.jackson.javax.json.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.github.pgelinas.jackson.javax.json.ConfigurationUtils;
import jakarta.json.JsonException;
import jakarta.json.stream.JsonGenerator;

import jakarta.json.stream.JsonGeneratorFactory;

public class JacksonGeneratorFactory implements JsonGeneratorFactory {
    private final JsonFactory _factory;
    private final Map<String, Object> _configInUse;

    public JacksonGeneratorFactory(JsonFactory factory) {
        _factory = factory;
        _configInUse = new HashMap<>();
    }

    public JacksonGeneratorFactory(Map<String, ?> config) {
        _factory = new JsonFactory();
        _configInUse = ConfigurationUtils.configure(_factory, config);
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        try {
            com.fasterxml.jackson.core.JsonGenerator generator = _factory.createGenerator(writer);
            return new JacksonGenerator(generator);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        try {
            com.fasterxml.jackson.core.JsonGenerator generator = _factory.createGenerator(out);
            return new JacksonGenerator(generator);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset) {
        try {
            com.fasterxml.jackson.core.JsonGenerator generator = _factory.createGenerator(new OutputStreamWriter(out, charset));
            return new JacksonGenerator(generator);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return _configInUse;
    }
}
