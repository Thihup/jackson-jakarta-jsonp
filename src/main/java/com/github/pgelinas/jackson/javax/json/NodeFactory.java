package com.github.pgelinas.jackson.javax.json;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map.Entry;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;

public final class NodeFactory {
    private JsonNodeFactory _nodeFactory;

	public NodeFactory(JsonNodeFactory nodeFactory) {
		_nodeFactory = nodeFactory;
	}

	public JsonValue from(JsonNode node) {
        JsonToken token = node.asToken();
        switch (token) {
            case START_OBJECT:
                return new JacksonObject((ObjectNode) node, this);
            case START_ARRAY:
                return new JacksonArray((ArrayNode) node, this);
            case VALUE_FALSE:
                return JsonValue.FALSE;
            case VALUE_NULL:
                return JsonValue.NULL;
            case VALUE_TRUE:
                return JsonValue.TRUE;
            case VALUE_STRING:
                return new JacksonString((TextNode) node);
            case VALUE_NUMBER_FLOAT:
            case VALUE_NUMBER_INT:
                return new JacksonNumber((NumericNode) node);
            default:
                throw new UnsupportedOperationException(format("Token '%s' isn't supported by the spec.", token));
        }
    }

	public JsonNode from(JsonValue value) {
		ValueType valueType = value.getValueType();
		switch (valueType) {
		case FALSE:
			return _nodeFactory.booleanNode(false);
		case TRUE:
			return _nodeFactory.booleanNode(true);
		case NULL:
			return _nodeFactory.nullNode();
		case STRING:
			return _nodeFactory.textNode(((JsonString) value).getString());
		case NUMBER: {
			if (value instanceof JacksonNumber)
				return ((JacksonNumber) value).delegate();
			return _nodeFactory.numberNode(((JsonNumber) value).bigDecimalValue());
		}case OBJECT:
			ObjectNode objectNode = _nodeFactory.objectNode();
			JsonObject jsonObject = (JsonObject) value;
			for (Entry<String, JsonValue> entry : jsonObject.entrySet()) {
				objectNode.put(entry.getKey(), from(entry.getValue()));
			}
			return objectNode;
		case ARRAY:
			ArrayNode arrayNode = _nodeFactory.arrayNode();
			JsonArray jsonArray = (JsonArray) value;
			for (JsonValue jsonValue : jsonArray) {
				arrayNode.add(from(jsonValue));
			}
			return arrayNode;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public JsonValue toJsonValue(Object value) {
		if (value == null) {
			return JsonValue.NULL;
		}
		if (value instanceof JsonValue) {
			return (JsonValue) value;
		}
		if (value instanceof String) {
			return new JacksonString(TextNode.valueOf(value.toString()));
		}
		if (value instanceof Integer) {
			return new JacksonNumber(IntNode.valueOf((Integer) value));
		}
		if (value instanceof Long) {
			return new JacksonNumber(LongNode.valueOf((Long) value));
		}
		if (value instanceof Boolean) {
			return (Boolean) value ? JsonValue.TRUE : JsonValue.FALSE;
		}
		if (value instanceof Double) {
			return new JacksonNumber(DoubleNode.valueOf((Double) value));
		}
		if (value instanceof BigDecimal) {
			return new JacksonNumber(DecimalNode.valueOf(((BigDecimal) value)));
		}
		if (value instanceof BigInteger) {
			return new JacksonNumber(BigIntegerNode.valueOf(((BigInteger) value)));
		}

		if (value instanceof JacksonValue) {
			return from(((JacksonValue<?>) value).delegate());
		}

		if (value instanceof ValueNode) {
			return from(((ValueNode) value));
		}

		if (value instanceof ArrayNode) {
			return new JacksonArray((ArrayNode) value, this);
		}

		if (value.getClass().isArray()) {
			ArrayNode arrayNode = _nodeFactory.arrayNode();
			if (value instanceof  int[]) {
				IntStream.of(((int[]) value)).forEach(arrayNode::add);
			}
			if (value instanceof double[]) {
				DoubleStream.of((double[]) value).forEach(arrayNode::add);
			}
			if (value instanceof Object[]) {
				Stream.of((Object[]) value)
					.map(this::toJsonValue)
					.map(this::from)
					.forEach(arrayNode::add);
			}
			return new JacksonArray(arrayNode, this);
		}

		throw new IllegalStateException();
	}

}
