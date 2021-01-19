package com.github.pgelinas.jackson.javax.json.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.pgelinas.jackson.javax.json.JacksonBuilderFactory;
import com.github.pgelinas.jackson.javax.json.JacksonNumber;
import com.github.pgelinas.jackson.javax.json.JacksonReaderFactory;
import com.github.pgelinas.jackson.javax.json.JacksonString;
import com.github.pgelinas.jackson.javax.json.JacksonWriterFactory;
import com.github.pgelinas.jackson.javax.json.NodeFactory;
import com.github.pgelinas.jackson.javax.json.stream.JacksonGeneratorFactory;
import com.github.pgelinas.jackson.javax.json.stream.JacksonParserFactory;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonString;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.spi.JsonProvider;

import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;

public class JacksonProvider extends JsonProvider {
    private final ObjectMapper _mapper = new ObjectMapper();
    private final NodeFactory _nodeFactory = new NodeFactory(_mapper.getNodeFactory());
    // Factories
    private final JacksonParserFactory _parserFactory;
    private final JacksonGeneratorFactory _generatorFactory;
    private final JacksonWriterFactory _writerFactory;
    private final JacksonReaderFactory _readerFactory;
    private final JacksonBuilderFactory _builderFactory;
    
    
    public JacksonProvider() {
        _parserFactory = new JacksonParserFactory(_mapper.getFactory(), _nodeFactory);
        _generatorFactory = new JacksonGeneratorFactory(_mapper.getFactory());
        _writerFactory = new JacksonWriterFactory(_mapper);
        _readerFactory = new JacksonReaderFactory(_mapper, _nodeFactory);
        _builderFactory = new JacksonBuilderFactory(_mapper, _nodeFactory);
    }

    @Override
    public JsonParser createParser(Reader reader) {
        return _parserFactory.createParser(reader);
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return _parserFactory.createParser(in);
    }

    @Override
    public JsonParserFactory createParserFactory(Map<String, ?> config) {
        return new JacksonParserFactory(config, _nodeFactory);
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return _generatorFactory.createGenerator(writer);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return _generatorFactory.createGenerator(out);
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
        return new JacksonGeneratorFactory(config);
    }

    @Override
    public JsonReader createReader(Reader reader) {
        return _readerFactory.createReader(reader);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return _readerFactory.createReader(in);
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        return _writerFactory.createWriter(writer);
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        return _writerFactory.createWriter(out);
    }

    @Override
    public JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        return new JacksonWriterFactory(config);
    }

    @Override
    public JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        return new JacksonReaderFactory(config);
    }

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return _builderFactory.createObjectBuilder();
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return _builderFactory.createArrayBuilder();
    }

    @Override
    public JsonBuilderFactory createBuilderFactory(Map<String, ?> config) {
        return new JacksonBuilderFactory(config);
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(JsonObject object) {
        return _builderFactory.createObjectBuilder(object);
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(Map<String, Object> map) {
        return _builderFactory.createObjectBuilder(map);
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(JsonArray array) {
        return _builderFactory.createArrayBuilder(array);
    }

    @Override
    public JsonString createValue(String value) {
        return new JacksonString(TextNode.valueOf(value));
    }

    @Override
    public JsonNumber createValue(int value) {
        return new JacksonNumber(IntNode.valueOf(value));
    }

    @Override
    public JsonNumber createValue(long value) {
        return new JacksonNumber(LongNode.valueOf(value));
    }

    @Override
    public JsonNumber createValue(double value) {
        return new JacksonNumber(DoubleNode.valueOf(value));
    }

    @Override
    public JsonNumber createValue(BigDecimal value) {
        return new JacksonNumber(DecimalNode.valueOf(value));
    }

    @Override
    public JsonNumber createValue(BigInteger value) {
        return new JacksonNumber(BigIntegerNode.valueOf(value));
    }
}
