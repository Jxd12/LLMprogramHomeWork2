// src/main/java/com/watermark/app/service/ConfigService.java
package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.WatermarkConfig;


import java.io.File;
import java.io.IOException;

// 在 ConfigService 中确保模板功能完整实现
public class ConfigService {
    private static final String TEMPLATE_DIR = "templates/";

    public ConfigService() {
        // 确保模板目录存在
        File dir = new File(TEMPLATE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // 确保 ConfigService 能正确处理所有字段
    public void saveTemplate(WatermarkConfig config, String templateName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File templateFile = new File(TEMPLATE_DIR + templateName + ".json");
            mapper.writeValue(templateFile, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WatermarkConfig loadTemplate(String templateName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File templateFile = new File(TEMPLATE_DIR + templateName + ".json");
            if (templateFile.exists()) {
                return mapper.readValue(templateFile, WatermarkConfig.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveLastConfiguration(WatermarkConfig config) {
        saveTemplate(config, "last_config");
    }

    public WatermarkConfig loadLastConfiguration() {
        return loadTemplate("last_config");
    }
}
