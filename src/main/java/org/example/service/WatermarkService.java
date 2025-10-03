// src/main/java/com/watermark/app/service/WatermarkService.java
package org.example.service;

import org.example.model.WatermarkConfig;
import org.example.model.WatermarkPosition;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class WatermarkService {

    // 在 addTextWatermark 方法中添加旋转处理
    public static BufferedImage addTextWatermark(BufferedImage original, WatermarkConfig config) {
        BufferedImage result = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 先绘制原始图像
        g2d.drawImage(original, 0, 0, null);

        // 计算文本尺寸
        Font font = new Font(config.getFontName(), getFontStyle(config.isBold(), config.isItalic()), config.getFontSize());
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        String text = config.getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // 计算水印位置
        // 替换原有的位置计算
        Point position = calculatePositionWithOffset(original, text, font, config.getPosition(),
                config.getOffsetX(), config.getOffsetY());
        // 应用旋转变换
        if (config.getRotation() != 0) {
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(config.getRotation()), position.x + textWidth/2, position.y - textHeight/2);
            g2d.setTransform(transform);
        }

        // 设置颜色和透明度
        Color awtColor = new Color(
                (float) config.getColor().getRed(),
                (float) config.getColor().getGreen(),
                (float) config.getColor().getBlue(),
                (float) config.getOpacity()
        );
        g2d.setColor(awtColor);

        // 绘制阴影效果
        if (config.isShadowEnabled()) {
            g2d.setColor(new Color(0, 0, 0, (int)(config.getOpacity() * 255 * 0.5)));
            g2d.drawString(text, position.x + 2, position.y + 2);
            g2d.setColor(awtColor);
        }

        // 绘制描边效果
        if (config.isStrokeEnabled()) {
            Stroke originalStroke = g2d.getStroke();
            Color originalColor = g2d.getColor();

            g2d.setStroke(new BasicStroke(1.0f));
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, position.x - 1, position.y);
            g2d.drawString(text, position.x + 1, position.y);
            g2d.drawString(text, position.x, position.y - 1);
            g2d.drawString(text, position.x, position.y + 1);

            g2d.setStroke(originalStroke);
            g2d.setColor(originalColor);
        }

        // 绘制主文本
        g2d.drawString(text, position.x, position.y);

        g2d.dispose();
        return result;
    }

    private static int getFontStyle(boolean bold, boolean italic) {
        int style = Font.PLAIN;
        if (bold) style |= Font.BOLD;
        if (italic) style |= Font.ITALIC;
        return style;
    }
    private static Point calculatePosition(BufferedImage image, String text, Font font, WatermarkPosition position) {
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.dispose();

        int x = 0, y = 0;
        int margin = 10; // 边距

        System.out.println("position: " + position);
        switch (position) {
            case TOP_LEFT:
                x = margin;
                y = textHeight + margin;
                break;
            case TOP_CENTER:
                x = (image.getWidth() - textWidth) / 2;
                y = textHeight + margin;
                break;
            case TOP_RIGHT:
                x = image.getWidth() - textWidth - margin;
                y = textHeight + margin;
                break;
            case CENTER_LEFT:
                x = margin;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case CENTER:
                x = (image.getWidth() - textWidth) / 2;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case CENTER_RIGHT:
                x = image.getWidth() - textWidth - margin;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case BOTTOM_LEFT:
                x = margin;
                y = image.getHeight() - margin;
                break;
            case BOTTOM_CENTER:
                x = (image.getWidth() - textWidth) / 2;
                y = image.getHeight() - margin;
                break;
            case BOTTOM_RIGHT:
                x = image.getWidth() - textWidth - margin;
                y = image.getHeight() - margin;
                break;
        }

        return new Point(x, y);
    }

    // 在 calculatePosition 方法后应用偏移量
    private static Point calculatePositionWithOffset(BufferedImage image, String text, Font font,
                                                     WatermarkPosition position, double offsetX, double offsetY) {
        Point basePosition = calculatePosition(image, text, font, position);
        basePosition.x += (int) offsetX;
        basePosition.y += (int) offsetY;
        return basePosition;
    }
}
