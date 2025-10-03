// src/main/java/com/watermark/app/controller/MainController.java
package org.example.controller;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.example.model.ExportOptions;
import org.example.model.FileItem;
import org.example.model.WatermarkConfig;
import org.example.model.WatermarkPosition;
import org.example.service.ConfigService;

import java.io.File;
import java.util.List;

public class MainController {
    
    // 图片列表相关控件
    @FXML
    private ListView<FileItem> imageListView;
    
    // 预览区域
    @FXML
    private ImageView previewImageView;
    
    // 文件导入导出控件
    @FXML
    private Button importButton;
    @FXML
    private Button batchImportButton;
    @FXML
    private TextField outputFolderField;
    @FXML
    private Button selectFolderButton;
    @FXML
    private RadioButton keepOriginalNameRadio;
    @FXML
    private RadioButton addPrefixRadio;
    @FXML
    private TextField prefixField;
    @FXML
    private RadioButton addSuffixRadio;
    @FXML
    private TextField suffixField;
    @FXML
    private ComboBox<String> formatComboBox;
    @FXML
    private Button exportButton;
    
    // 水印设置控件
    @FXML
    private TextField watermarkTextField;
    @FXML
    private ComboBox<String> fontComboBox;
    @FXML
    private ComboBox<Integer> fontSizeComboBox;
    @FXML
    private CheckBox boldCheckBox;
    @FXML
    private CheckBox italicCheckBox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Slider opacitySlider;
    @FXML
    private CheckBox shadowCheckBox;
    @FXML
    private CheckBox strokeCheckBox;
    
    // 布局控制控件
    @FXML
    private Button topLeftButton;
    @FXML
    private Button topCenterButton;
    @FXML
    private Button topRightButton;
    @FXML
    private Button centerLeftButton;
    @FXML
    private Button centerButton;
    @FXML
    private Button centerRightButton;
    @FXML
    private Button bottomLeftButton;
    @FXML
    private Button bottomCenterButton;
    @FXML
    private Button bottomRightButton;
    @FXML
    private Slider rotationSlider;
    @FXML
    private TextField rotationField;
    
    // 模板管理控件
    @FXML
    private TextField templateNameField;
    @FXML
    private Button saveTemplateButton;
    @FXML
    private ComboBox<String> templateComboBox;
    @FXML
    private Button loadTemplateButton;
    @FXML
    private Button deleteTemplateButton;
    
    // 数据模型
    private ObservableList<FileItem> imageList = FXCollections.observableArrayList();
    private WatermarkConfig currentConfig = new WatermarkConfig();
    private ConfigService configService = new ConfigService();
    
    @FXML
    public void initialize() {
        // 初始化界面组件
        setupUIComponents();
        // 绑定事件监听器
        bindEventListeners();
        // 加载系统字体列表
        loadSystemFonts();
        // 加载模板列表
        loadTemplateList();
        // 在 initialize 方法中添加列表选择监听器
        imageListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        updatePreview(newValue);
                    }
                }
        );
    }
    
    private void setupUIComponents() {
        // 设置图片列表
        imageListView.setItems(imageList);
        
        // 设置字体大小选项
        fontSizeComboBox.setItems(FXCollections.observableArrayList(8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72));
        fontSizeComboBox.setValue(24);
        
        // 设置输出格式选项
        formatComboBox.setItems(FXCollections.observableArrayList("JPEG", "PNG"));
        formatComboBox.setValue("PNG");
        
        // 设置默认颜色
        colorPicker.setValue(Color.BLACK);
        
        // 设置透明度滑块
        opacitySlider.setValue(50);
    }
    
    private void bindEventListeners() {
        // 文件导入按钮事件
        importButton.setOnAction(e -> importSingleImage());
        batchImportButton.setOnAction(e -> importMultipleImages());
        selectFolderButton.setOnAction(e -> selectOutputFolder());
        exportButton.setOnAction(e -> exportImages());
        
        // 水印文本变化事件
        watermarkTextField.textProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        
        // 字体设置变化事件
        fontComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        fontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        boldCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        italicCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        shadowCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        strokeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateWatermarkPreview());
        
        // 位置按钮事件
        topLeftButton.setOnAction(e -> setPosition(WatermarkPosition.TOP_LEFT));
        topCenterButton.setOnAction(e -> setPosition(WatermarkPosition.TOP_CENTER));
        topRightButton.setOnAction(e -> setPosition(WatermarkPosition.TOP_RIGHT));
        centerLeftButton.setOnAction(e -> setPosition(WatermarkPosition.CENTER_LEFT));
        centerButton.setOnAction(e -> setPosition(WatermarkPosition.CENTER));
        centerRightButton.setOnAction(e -> setPosition(WatermarkPosition.CENTER_RIGHT));
        bottomLeftButton.setOnAction(e -> setPosition(WatermarkPosition.BOTTOM_LEFT));
        bottomCenterButton.setOnAction(e -> setPosition(WatermarkPosition.BOTTOM_CENTER));
        bottomRightButton.setOnAction(e -> setPosition(WatermarkPosition.BOTTOM_RIGHT));
        
        // 旋转控制事件
        rotationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rotationField.setText(String.valueOf(newValue.intValue()));
            updateWatermarkPreview();
        });
        rotationField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                rotationSlider.setValue(Math.max(0, Math.min(360, value)));
                updateWatermarkPreview();
            } catch (NumberFormatException e) {
                // 忽略无效输入
            }
        });
        
        // 模板管理事件
        saveTemplateButton.setOnAction(e -> saveCurrentTemplate());
        loadTemplateButton.setOnAction(e -> loadSelectedTemplate());
        deleteTemplateButton.setOnAction(e -> deleteSelectedTemplate());
    }
    
    private void loadSystemFonts() {
        // 添加一些常用字体作为示例
        fontComboBox.setItems(FXCollections.observableArrayList(
            "Arial", "Times New Roman", "Courier New", "Verdana", "Helvetica"
        ));
        fontComboBox.setValue("Arial");
    }
    
    private void loadTemplateList() {
        // 这里应该从文件系统加载模板列表
        templateComboBox.setItems(FXCollections.observableArrayList(
            "默认模板", "公司水印", "个人水印"
        ));
        templateComboBox.setValue("默认模板");
    }
    
    private void importSingleImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片文件");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("图片文件", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.tiff", "*.tif")
        );
        
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(importButton.getScene().getWindow());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                imageList.add(new FileItem(file));
            }
            // 如果是第一张图片，显示预览
            if (imageList.size() > 0 && imageListView.getSelectionModel().getSelectedItem() == null) {
                imageListView.getSelectionModel().select(0);
                updatePreview(imageList.get(0));
            }
        }
    }
    
    private void importMultipleImages() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择图片文件夹");
        
        File selectedDirectory = directoryChooser.showDialog(batchImportButton.getScene().getWindow());
        if (selectedDirectory != null) {
            // 这里应该遍历文件夹中的所有图片文件
            // 为简化实现，这里只是示例
            System.out.println("批量导入功能待实现: " + selectedDirectory.getAbsolutePath());
        }
    }
    
    private void selectOutputFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择输出文件夹");
        
        File selectedDirectory = directoryChooser.showDialog(selectFolderButton.getScene().getWindow());
        if (selectedDirectory != null) {
            outputFolderField.setText(selectedDirectory.getAbsolutePath());
        }
    }
    
    private void exportImages() {
        if (imageList.isEmpty()) {
            showAlert("提示", "请先导入图片");
            return;
        }
        
        String outputFolder = outputFolderField.getText();
        if (outputFolder.isEmpty()) {
            showAlert("提示", "请选择输出文件夹");
            return;
        }
        
        ExportOptions options = new ExportOptions();
        options.setOutputFolder(outputFolder);
        options.setOutputFormat(formatComboBox.getValue());
        options.setWatermarkConfig(getCurrentWatermarkConfig());
        
        // 设置命名规则
        if (keepOriginalNameRadio.isSelected()) {
            options.setNamingRule("original");
        } else if (addPrefixRadio.isSelected()) {
            options.setNamingRule("prefix");
            options.setPrefix(prefixField.getText());
        } else if (addSuffixRadio.isSelected()) {
            options.setNamingRule("suffix");
            options.setSuffix(suffixField.getText());
        }
        
        // 这里应该调用实际的导出逻辑
        System.out.println("导出图片功能待实现");
        showAlert("提示", "导出功能已触发，请查看控制台输出");
    }
    
    private void updateWatermarkPreview() {
        currentConfig = getCurrentWatermarkConfig();
        FileItem selectedItem = imageListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            updatePreview(selectedItem);
        }
    }
    
    private WatermarkConfig getCurrentWatermarkConfig() {
        WatermarkConfig config = new WatermarkConfig();
        config.setText(watermarkTextField.getText());
        config.setFontName(fontComboBox.getValue());
        config.setFontSize(fontSizeComboBox.getValue());
        config.setBold(boldCheckBox.isSelected());
        config.setItalic(italicCheckBox.isSelected());
        config.setColor(colorPicker.getValue());
        config.setOpacity(opacitySlider.getValue() / 100.0);
        config.setShadowEnabled(shadowCheckBox.isSelected());
        config.setStrokeEnabled(strokeCheckBox.isSelected());
        config.setRotation(rotationSlider.getValue());
        return config;
    }
    
    private void updatePreview(FileItem fileItem) {
        try {
            // 从文件创建 Image 对象
            Image image = new Image(fileItem.getFile().toURI().toString());
            previewImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            // 显示错误图片或提示
            previewImageView.setImage(null);
        }
    }
    
    private void setPosition(WatermarkPosition position) {
        currentConfig.setPosition(position);
        updateWatermarkPreview();
    }
    
    private void saveCurrentTemplate() {
        String templateName = templateNameField.getText();
        if (templateName.isEmpty()) {
            showAlert("提示", "请输入模板名称");
            return;
        }
        
        configService.saveTemplate(getCurrentWatermarkConfig(), templateName);
        loadTemplateList(); // 重新加载模板列表
        showAlert("提示", "模板保存成功");
    }
    
    private void loadSelectedTemplate() {
        String selectedTemplate = templateComboBox.getValue();
        if (selectedTemplate != null) {
            WatermarkConfig config = configService.loadTemplate(selectedTemplate);
            if (config != null) {
                applyWatermarkConfig(config);
                showAlert("提示", "模板加载成功");
            }
        }
    }
    
    private void deleteSelectedTemplate() {
        String selectedTemplate = templateComboBox.getValue();
        if (selectedTemplate != null) {
            // 这里应该实现实际的模板删除逻辑
            System.out.println("删除模板: " + selectedTemplate);
            showAlert("提示", "模板删除成功");
        }
    }
    
    private void applyWatermarkConfig(WatermarkConfig config) {
        watermarkTextField.setText(config.getText());
        fontComboBox.setValue(config.getFontName());
        fontSizeComboBox.setValue(config.getFontSize());
        boldCheckBox.setSelected(config.isBold());
        italicCheckBox.setSelected(config.isItalic());
        colorPicker.setValue(config.getColor());
        opacitySlider.setValue(config.getOpacity() * 100);
        shadowCheckBox.setSelected(config.isShadowEnabled());
        strokeCheckBox.setSelected(config.isStrokeEnabled());
        rotationSlider.setValue(config.getRotation());
    }
    
    public void loadLastConfiguration() {
        WatermarkConfig config = configService.loadLastConfiguration();
        if (config != null) {
            applyWatermarkConfig(config);
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
