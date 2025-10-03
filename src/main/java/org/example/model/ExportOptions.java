// src/main/java/com/watermark/app/model/ExportOptions.java
package org.example.model;

public class ExportOptions {
    private String outputFolder;
    private String outputFormat;
    private String namingRule;
    private String prefix;
    private String suffix;
    private int jpegQuality;
    private WatermarkConfig watermarkConfig;
    
    // Getters and Setters
    public String getOutputFolder() { return outputFolder; }
    public void setOutputFolder(String outputFolder) { this.outputFolder = outputFolder; }
    
    public String getOutputFormat() { return outputFormat; }
    public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
    
    public String getNamingRule() { return namingRule; }
    public void setNamingRule(String namingRule) { this.namingRule = namingRule; }
    
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    
    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }
    
    public int getJpegQuality() { return jpegQuality; }
    public void setJpegQuality(int jpegQuality) { this.jpegQuality = jpegQuality; }
    
    public WatermarkConfig getWatermarkConfig() { return watermarkConfig; }
    public void setWatermarkConfig(WatermarkConfig watermarkConfig) { this.watermarkConfig = watermarkConfig; }
}
