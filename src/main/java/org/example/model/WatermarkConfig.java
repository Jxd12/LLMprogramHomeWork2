// src/main/java/com/watermark/app/model/WatermarkConfig.java
package org.example.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.scene.paint.Color;
import org.example.util.ColorDeserializer;
import org.example.util.ColorSerializer;

public class WatermarkConfig {
    private String text = "水印文字";
    private String fontName = "Arial";
    private int fontSize = 24;
    private boolean bold = false;
    private boolean italic = false;

    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    private Color color = Color.BLACK;
    private double opacity = 0.5;
    private WatermarkPosition position = WatermarkPosition.CENTER;
    private double rotation = 0.0;
    private boolean shadowEnabled = false;
    private boolean strokeEnabled = false;

    // 在 WatermarkConfig 类中添加
    private double offsetX = 0.0;
    private double offsetY = 0.0;

    // 构造函数
    public WatermarkConfig() {}

    // Getters and Setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getFontName() { return fontName; }
    public void setFontName(String fontName) { this.fontName = fontName; }

    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }

    public boolean isBold() { return bold; }
    public void setBold(boolean bold) { this.bold = bold; }

    public boolean isItalic() { return italic; }
    public void setItalic(boolean italic) { this.italic = italic; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public double getOpacity() { return opacity; }
    public void setOpacity(double opacity) { this.opacity = opacity; }

    public WatermarkPosition getPosition() { return position; }
    public void setPosition(WatermarkPosition position) { this.position = position;}

    public double getRotation() { return rotation; }
    public void setRotation(double rotation) { this.rotation = rotation; }

    public boolean isShadowEnabled() { return shadowEnabled; }
    public void setShadowEnabled(boolean shadowEnabled) { this.shadowEnabled = shadowEnabled; }

    public boolean isStrokeEnabled() { return strokeEnabled; }
    public void setStrokeEnabled(boolean strokeEnabled) { this.strokeEnabled = strokeEnabled; }
    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }
}
