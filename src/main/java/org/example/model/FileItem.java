// src/main/java/com/watermark/app/model/FileItem.java
package org.example.model;

import javafx.scene.image.Image;

import java.io.File;

public class FileItem {
    private File file;
    private Image thumbnail;
    private String fileName;
    
    public FileItem(File file) {
        this.file = file;
        this.fileName = file.getName();
        // 这里可以添加缩略图生成逻辑
    }
    
    // Getters and Setters
    public File getFile() { return file; }
    public void setFile(File file) { this.file = file; }
    
    public Image getThumbnail() { return thumbnail; }
    public void setThumbnail(Image thumbnail) { this.thumbnail = thumbnail; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}
