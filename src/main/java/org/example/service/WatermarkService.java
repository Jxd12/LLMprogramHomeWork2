// src/main/java/com/watermark/app/service/WatermarkService.java
package org.example.service;

import org.example.model.WatermarkConfig;
import org.example.model.WatermarkPosition;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WatermarkService {

    public static BufferedImage addTextWatermark(BufferedImage original, WatermarkConfig config) {
        BufferedImage result = new BufferedImage(
            original.getWidth(),
            original.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(original, 0, 0, null);

        // 设置字体和颜色
        int style = Font.PLAIN;
        if (config.isBold() && config.isItalic()) {
            style = Font.BOLD | Font.ITALIC;
        } else if (config.isBold()) {
            style = Font.BOLD;
        } else if (config.isItalic()) {
            style = Font.ITALIC;
        }

        Font font = new Font(config.getFontName(), style, config.getFontSize());
        g2d.setFont(font);

        // 转换JavaFX颜色到AWT颜色
        java.awt.Color awtColor = new java.awt.Color(
            (float) config.getColor().getRed(),
            (float) config.getColor().getGreen(),
            (float) config.getColor().getBlue(),
            (float) config.getOpacity()
        );
        g2d.setColor(awtColor);

        // 计算水印位置
        Point position = calculatePosition(original, config.getText(), font, config.getPosition());

        // 绘制阴影效果
        if (config.isShadowEnabled()) {
            g2d.setColor(new java.awt.Color(0, 0, 0, (int)(config.getOpacity() * 128)));
            g2d.drawString(config.getText(), position.x + 2, position.y + 2);
            g2d.setColor(awtColor);
        }

        // 绘制文本
        g2d.drawString(config.getText(), position.x, position.y);

        g2d.dispose();
        return result;
    }

    private static Point calculatePosition(BufferedImage image, String text, Font font, WatermarkPosition position) {
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.dispose();

        int x = 0, y = 0;
        switch (position) {
            case TOP_LEFT:
                x = 10;
                y = textHeight;
                break;
            case TOP_CENTER:
                x = (image.getWidth() - textWidth) / 2;
                y = textHeight;
                break;
            case TOP_RIGHT:
                x = image.getWidth() - textWidth - 10;
                y = textHeight;
                break;
            case CENTER_LEFT:
                x = 10;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case CENTER:
                x = (image.getWidth() - textWidth) / 2;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case CENTER_RIGHT:
                x = image.getWidth() - textWidth - 10;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case BOTTOM_LEFT:
                x = 10;
                y = image.getHeight() - 10;
                break;
            case BOTTOM_CENTER:
                x = (image.getWidth() - textWidth) / 2;
                y = image.getHeight() - 10;
                break;
            case BOTTOM_RIGHT:
                x = image.getWidth() - textWidth - 10;
                y = image.getHeight() - 10;
                break;
        }

        return new Point(x, y);
    }
}
