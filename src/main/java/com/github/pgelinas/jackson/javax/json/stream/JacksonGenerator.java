package com.github.pgelinas.jackson.javax.json.stream;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonStreamContext;
import com.github.pgelinas.jackson.javax.json.JacksonValue;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;
import jakarta.json.stream.JsonGenerationException;
import jakarta.json.stream.JsonGenerator;

public class JacksonGenerator implements JsonGenerator {
    private final com.fasterxml.jackson.core.JsonGenerator _generator;

    public JacksonGenerator(com.fasterxml.jackson.core.JsonGenerator generator) {
        _generator = generator;
    }

    private int startArrayCounter = 0;
    private int startObjectCounter = 0;

    @Override
    public JsonGenerator writeStartObject() {
        try {
            startObjectCounter++;
            _generator.writeStartObject();
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator writeStartObject(String name) {
        try {
            startObjectCounter++;
            _generator.writeFieldName(name);
            _generator.writeStartObject();
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator writeStartArray() {
        try {
            checkValidPosition();
            startArrayCounter++;
            _generator.writeStartArray();
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator writeStartArray(String name) {
        try {
            checkValidPosition();
            startArrayCounter++;
            _generator.writeFieldName(name);
            _generator.writeStartArray();
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, JsonValue value) {
        if (value == null) throw new NullPointerException();

        // Fast track, implementation is Jackson-backed.
        if (value instanceof JacksonValue) {
            try {
                _generator.writeFieldName(name);
                _generator.writeTree(((JacksonValue<?>) value).delegate());
            } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
                throw new JsonGenerationException("", e);
            } catch (IOException e) {
                throw new JsonException("", e);
            }
            return this;
        }

        // Slower track, implementation is not backed by Jackson and we need to generate json manually.
        ValueType type = value.getValueType();
        switch (type) {
            case NUMBER:
                write(name, ((JsonNumber) value).bigDecimalValue());
                break;
            case STRING:
                write(name, ((JsonString) value).getString());
                break;
            case NULL:
                writeNull(name);
                break;
            case FALSE:
                write(name, false);
                break;
            case TRUE:
                write(name, true);
                break;
            case ARRAY:
                writeStartArray(name);
                writeArray((JsonArray) value);
                writeEnd();
                break;
            case OBJECT:
                writeStartObject(name);
                writeObject((JsonObject) value);
                writeEnd();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return this;
    }

    @Override
    public JsonGenerator write(String name, String value) {
        try {
            _generator.writeStringField(name, value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, BigInteger value) {
        try {
            _generator.writeFieldName(name);
            _generator.writeNumber(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, BigDecimal value) {
        try {
            _generator.writeNumberField(name, value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, int value) {
        try {
            _generator.writeNumberField(name, value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, long value) {
        try {
            _generator.writeNumberField(name, value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, double value) {
        checkValidDouble(value);
        try {
            _generator.writeNumberField(name, value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, boolean value) {
        try {
            _generator.writeBooleanField(name, value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator writeNull(String name) {
        try {
            _generator.writeNullField(name);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator writeEnd() {
        JsonStreamContext context = _generator.getOutputContext();
        try {
            if (context.inObject()) {
                _generator.writeEndObject();
                startObjectCounter--;
            } else if (context.inArray()) {
                _generator.writeEndArray();
                startArrayCounter--;
            } else {
                throw new JsonGenerationException("No end marker to write for root-level value.");
            }
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(JsonValue value) {
        if (value == null) throw new NullPointerException();

        // Fast track, implementation is Jackson-backed.
        if (value instanceof JacksonValue) {
            try {
                _generator.writeTree(((JacksonValue<?>) value).delegate());
            } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
                throw new JsonGenerationException("", e);
            } catch (IOException e) {
                throw new JsonException("", e);
            }
            return this;
        }

        // Slower track, implementation is not backed by Jackson and we need to generate json manually.
        ValueType type = value.getValueType();
        switch (type) {
            case NUMBER:
                write(((JsonNumber) value).bigDecimalValue());
                break;
            case STRING:
                write(((JsonString) value).getString());
                break;
            case NULL:
                writeNull();
                break;
            case FALSE:
                write(false);
                break;
            case TRUE:
                write(true);
                break;
            case ARRAY:
                writeStartArray();
                writeArray((JsonArray) value);
                writeEnd();
                break;
            case OBJECT:
                writeStartObject();
                writeObject((JsonObject) value);
                writeEnd();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return this;

    }

    private void writeObject(JsonObject value) {
        for (Entry<String, JsonValue> entry : value.entrySet()) {
            write(entry.getKey(), entry.getValue());
        }
    }

    private void writeArray(JsonArray value) {
        for (JsonValue jsonValue : value) {
            write(jsonValue);
        }
    }

    @Override
    public JsonGenerator write(String value) {
        try {
            _generator.writeString(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(BigDecimal value) {
        checkInObjectOrArray();
        try {
            _generator.writeNumber(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(BigInteger value) {
        checkInObjectOrArray();
        try {
            _generator.writeNumber(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(int value) {
        checkInObjectOrArray();
        try {
            _generator.writeNumber(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(long value) {
        checkInObjectOrArray();
        try {
            _generator.writeNumber(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(double value) {
        checkValidDouble(value);
        checkInObjectOrArray();
        try {
            _generator.writeNumber(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(boolean value) {
        checkInObjectOrArray();
        try {
            _generator.writeBoolean(value);
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public JsonGenerator writeNull() {
        checkValidPosition();
        checkInObjectOrArray();
        try {
            _generator.writeNull();
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
        return this;
    }

    @Override
    public void close() {
        if (startObjectCounter != 0 || startArrayCounter != 0) {
            throw new JsonGenerationException("Invalid JSON");
        }
        try {
            _generator.close();
        } catch (com.fasterxml.jackson.core.JsonGenerationException e) {
            throw new JsonGenerationException("", e);
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public void flush() {
        try {
            _generator.flush();
        } catch (IOException e) {
            throw new JsonException("", e);
        }
    }

    @Override
    public JsonGenerator writeKey(String name) {
        try {
            _generator.writeFieldName(name);
        } catch (IOException e) {
            throw new JsonGenerationException("", e);
        }
        return this;
    }

    public com.fasterxml.jackson.core.JsonGenerator delegate() {
        return _generator;
    }

    private void checkValidDouble(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value))
            throw new NumberFormatException();
    }

    private void checkValidPosition() {
        JsonStreamContext outputContext = _generator.getOutputContext();
        if (outputContext.inObject() && !outputContext.hasCurrentName()) {
            throw new JsonGenerationException("Invalid JSON");
        }
    }

    private void checkInObjectOrArray() {
        if (startArrayCounter == 0 && startObjectCounter == 0)
            throw new JsonGenerationException("Invalid JSON");
    }

}
