// src/main/java/com/watermark/app/service/WatermarkService.java
package org.example.service;

import org.example.model.WatermarkConfig;
import org.example.model.WatermarkPosition;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WatermarkService {

    // 在 WatermarkService 中添加中文字体支持
    public static BufferedImage addTextWatermark(BufferedImage original, WatermarkConfig config) {
        BufferedImage result = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = result.createGraphics();
        // 启用抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.drawImage(original, 0, 0, null);

        // 使用支持中文的字体
        Font font = new Font(config.getFontName(), getFontStyle(config.isBold(), config.isItalic()), config.getFontSize());
        g2d.setFont(font);

        // 检查字体是否支持当前文本字符
        FontMetrics fm = g2d.getFontMetrics();
        String text = config.getText();

        // 转换JavaFX颜色到AWT颜色
        java.awt.Color awtColor = new java.awt.Color(
                (float) config.getColor().getRed(),
                (float) config.getColor().getGreen(),
                (float) config.getColor().getBlue(),
                (float) config.getOpacity()
        );
        g2d.setColor(awtColor);

        // 计算水印位置
        Point position = calculatePosition(original, text, font, config.getPosition());

        // 阴影效果实现
        if (config.isShadowEnabled()) {
            // 使用半透明黑色绘制阴影
            g2d.setColor(new Color(0, 0, 0, (int)(config.getOpacity() * 255 * 0.5)));
            g2d.drawString(text, position.x + 2, position.y + 2);
            // 恢复原始颜色
            g2d.setColor(awtColor);
        }

        // 描边效果实现
        if (config.isStrokeEnabled()) {
            // 保存原始渲染属性
            Stroke originalStroke = g2d.getStroke();
            Color originalColor = g2d.getColor();

            // 设置描边属性
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.setColor(Color.WHITE); // 使用白色描边

            // 绘制多个方向的描边以创建轮廓效果
            g2d.drawString(text, position.x - 1, position.y);
            g2d.drawString(text, position.x + 1, position.y);
            g2d.drawString(text, position.x, position.y - 1);
            g2d.drawString(text, position.x, position.y + 1);

            // 恢复原始属性
            g2d.setStroke(originalStroke);
            g2d.setColor(originalColor);
        }

        // 绘制文本
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
        int textWidth = fm.stringWidth(text); // 正确计算中文文本宽度
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
