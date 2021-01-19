package com.github.pgelinas.jackson.javax.json;

import static java.lang.String.format;

import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
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
		case NUMBER:
			return _nodeFactory.numberNode(((JsonNumber) value).bigDecimalValue());
		case OBJECT:
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
}
