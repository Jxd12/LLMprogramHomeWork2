package org.example.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.paint.Color;

import java.io.IOException;

// ColorDeserializer.java
public class ColorDeserializer extends JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node != null && !node.isNull()) {
            double red = node.get("red").asDouble();
            double green = node.get("green").asDouble();
            double blue = node.get("blue").asDouble();
            double opacity = node.get("opacity").asDouble();
            return new Color(red, green, blue, opacity);
        }
        return Color.BLACK; // 默认颜色
    }
}
