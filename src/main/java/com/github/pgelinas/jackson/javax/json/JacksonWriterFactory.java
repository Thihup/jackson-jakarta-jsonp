package com.github.pgelinas.jackson.javax.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonException;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;

public class JacksonWriterFactory implements JsonWriterFactory {
    private final ObjectMapper _mapper;
    private Map<String, Object> _configInUse;

    public JacksonWriterFactory(Map<String, ?> config) {
        this(new ObjectMapper());
        _configInUse = ConfigurationUtils.configure(_mapper, config);
    }

    public JacksonWriterFactory(ObjectMapper mapper) {
        _mapper = mapper;
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        try {
            return new JacksonWriter(_mapper, writer);
        } catch (IOException exception) {
            throw new JsonException("", exception);
        }
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        try {
            return new JacksonWriter(_mapper, out);
        } catch (IOException exception) {
            throw new JsonException("", exception);
        }
    }

    @Override
    public JsonWriter createWriter(OutputStream out, Charset charset) {
        return createWriter(new OutputStreamWriter(out, charset));
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return _configInUse;
    }
}
