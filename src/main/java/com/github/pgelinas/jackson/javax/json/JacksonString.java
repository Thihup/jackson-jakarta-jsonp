package com.github.pgelinas.jackson.javax.json;

import com.fasterxml.jackson.databind.node.TextNode;
import jakarta.json.JsonString;

public class JacksonString extends JacksonValueNode<TextNode> implements JsonString {
    public JacksonString(TextNode node) {
        super(node);
    }

    @Override
    public String getString() {
        return _delegate.asText();
    }

    @Override
    public CharSequence getChars() {
        return _delegate.asText();
    }
}
