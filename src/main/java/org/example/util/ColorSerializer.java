package org.example.util;// 创建新的类 org.example.util.ColorSerializer.java
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import javafx.scene.paint.Color;

import java.io.IOException;

// org.example.util.ColorSerializer.java
public class ColorSerializer extends JsonSerializer<Color> {
    @Override
    public void serialize(Color color, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (color != null) {
            gen.writeStartObject();
            gen.writeNumberField("red", color.getRed());
            gen.writeNumberField("green", color.getGreen());
            gen.writeNumberField("blue", color.getBlue());
            gen.writeNumberField("opacity", color.getOpacity());
            gen.writeEndObject();
        } else {
            gen.writeNull();
        }
    }
}

