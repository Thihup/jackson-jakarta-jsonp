package com.github.pgelinas.jackson.javax.json;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class ConfigurationUtils {
    private ConfigurationUtils() {}

    private static JsonFactory.Feature findFactoryFeature(String name) {
        return findEnum(name, Arrays.asList(JsonFactory.Feature.values()));
    }

    private static JsonParser.Feature findParserFeature(String name) {
        return findEnum(name, Arrays.asList(JsonParser.Feature.values()));
    }

    private static JsonGenerator.Feature findGeneratorFeature(String name) {
        return findEnum(name, Arrays.asList(JsonGenerator.Feature.values()));
    }

    private static MapperFeature findMapperFeature(String name) {
        return findEnum(name, Arrays.asList(MapperFeature.values()));
    }

    private static DeserializationFeature findDeserializationFeature(String name) {
        return findEnum(name, Arrays.asList(DeserializationFeature.values()));
    }

    private static SerializationFeature findSerializationFeature(String name) {
        return findEnum(name, Arrays.asList(SerializationFeature.values()));
    }

    private static <T extends Enum<T>> T findEnum(String name, List<T> values) {
        for (T val : values) {
            if (val.name().equals(name)) return val;
        }
        return null;
    }

    public static Map<String, Boolean> mapperConfiguration() {
        Map<String, Boolean> config = new HashMap<String, Boolean>();
        MapperFeature[] mapperFeatures = MapperFeature.values();
        DeserializationFeature[] deserializationFeatures = DeserializationFeature.values();
        SerializationFeature[] serializationFeatures = SerializationFeature.values();
        for (SerializationFeature feature : serializationFeatures) {
            config.put(feature.name(), feature.enabledByDefault());
        }
        for (DeserializationFeature feature : deserializationFeatures) {
            config.put(feature.name(), feature.enabledByDefault());
        }
        for (MapperFeature feature : mapperFeatures) {
            config.put(feature.name(), feature.enabledByDefault());
        }
        config.putAll(factoryConfiguration());
        return config;
    }

    public static Map<String, Boolean> factoryConfiguration() {
        JsonParser.Feature[] parserFeatures = JsonParser.Feature.values();
        JsonFactory.Feature[] factoryFeatures = JsonFactory.Feature.values();
        JsonGenerator.Feature[] generatorFeatures = JsonGenerator.Feature.values();
        Map<String, Boolean> config = new HashMap<String, Boolean>();
        for (JsonFactory.Feature feature : factoryFeatures) {
            config.put(feature.name(), feature.enabledByDefault());
        }
        for (JsonParser.Feature feature : parserFeatures) {
            config.put(feature.name(), feature.enabledByDefault());
        }
        for (JsonGenerator.Feature feature : generatorFeatures) {
            config.put(feature.name(), feature.enabledByDefault());
        }
        return config;
    }

    public static Map<String, Object> configure(JsonFactory factory, Map<String, ?> config) {
        if (config == null) return Collections.emptyMap();
        Map<String, Object> inUse = new HashMap<>();
        for (Entry<String, ?> entry : config.entrySet()) {
            String featureName = entry.getKey();
            Object value = entry.getValue();
            if (!(value instanceof Boolean)) continue;
            Boolean state = (Boolean) value;
            if (configure(factory, featureName, state)) {
                inUse.put(featureName, state);
            }
        }
        return inUse;
    }

    public static Map<String, Object> configure(ObjectMapper mapper, Map<String, ?> config) {
        if (config == null) return Collections.emptyMap();
        Map<String, Object> inUse = new HashMap<>();
        for (Entry<String, ?> entry : config.entrySet()) {
            String featureName = entry.getKey();
            Object value = entry.getValue();
            if (!(value instanceof Boolean)) continue;
            boolean state = (Boolean) value;
            if (configure(mapper, featureName, state) || configure(mapper.getFactory(), featureName, state)) {
                inUse.put(featureName, state);
            }
        }
        return inUse;
    }

    private static boolean configure(ObjectMapper mapper, String featureName, boolean state) {
        MapperFeature mapperFeature = findMapperFeature(featureName);
        if (mapperFeature != null) {
            mapper.configure(mapperFeature, state);
            return true;
        }
        DeserializationFeature deserializationFeature = findDeserializationFeature(featureName);
        if (deserializationFeature != null) {
            mapper.configure(deserializationFeature, state);
            return true;
        }
        SerializationFeature serializationFeature = findSerializationFeature(featureName);
        if (serializationFeature != null) {
            mapper.configure(serializationFeature, state);
            return true;
        }

        if (jakarta.json.stream.JsonGenerator.PRETTY_PRINTING.equals(featureName)) {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, state);
            return true;
        }

        return false;
    }

    private static boolean configure(JsonFactory factory, String featureName, boolean state) {
        JsonFactory.Feature factoryFeature = findFactoryFeature(featureName);
        if (factoryFeature != null) {
            factory.configure(factoryFeature, state);
            return true;
        }
        JsonParser.Feature parserFeature = findParserFeature(featureName);
        if (parserFeature != null) {
            factory.configure(parserFeature, state);
            return true;
        }
        JsonGenerator.Feature generatorFeature = findGeneratorFeature(featureName);
        if (generatorFeature != null) {
            factory.configure(generatorFeature, state);
            return true;
        }

        if (jakarta.json.stream.JsonGenerator.PRETTY_PRINTING.equals(featureName)) {
            // TODO need to implement it
            return true;
        }

        return false;
    }
}
