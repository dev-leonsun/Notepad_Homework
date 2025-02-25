package com.notepad.core;

import com.notepad.view.EditorPane;
import com.notepad.view.MainFrame;
import com.notepad.view.TabHeader;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.*;

public class FileController {
    private static final String FILE_EXTENSION = "np";
    private static final String FILE_DESCRIPTION = "Notepad文件 (*.np)";

    public static void saveFile(EditorPane editor, JFrame parent) {
        if (editor == null) {
            showError("没有可保存的编辑器");
            return;
        }

        if (editor.getCurrentFile() == null) {
            saveAsFile(editor, parent);
        } else {
            performSave(editor, editor.getCurrentFile());
        }
    }

    public static void saveAsFile(EditorPane editor, JFrame parent) {
        if (editor == null) {
            showError("没有可保存的编辑器");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        // 设置文件过滤器，只显示.np文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter(FILE_DESCRIPTION, FILE_EXTENSION);
        chooser.setFileFilter(filter);
        
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            // 检查文件是否有.np后缀，如果没有则添加
            if (!selectedFile.getName().toLowerCase().endsWith("." + FILE_EXTENSION)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + "." + FILE_EXTENSION);
            }
            performSave(editor, selectedFile);
            editor.setCurrentFile(selectedFile);
            
            // 更新标签页标题
            updateTabTitle(editor, selectedFile);
        }
    }

    private static void performSave(EditorPane editor, File file) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {

            writer.write(editor.getText());
            editor.setCurrentFile(file);
            editor.setModified(false);
            
            // 显示保存成功消息
            JOptionPane.showMessageDialog(null, 
                "文件已成功保存为: " + file.getName(), 
                "保存成功", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showError("保存失败: " + e.getMessage());
        }
    }
    
    public static void openFile(MainFrame frame) {
        JFileChooser chooser = new JFileChooser();
        // 设置文件过滤器，只显示.np文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter(FILE_DESCRIPTION, FILE_EXTENSION);
        chooser.setFileFilter(filter);
        
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            
            // 检查文件是否有.np后缀
            if (!selectedFile.getName().toLowerCase().endsWith("." + FILE_EXTENSION)) {
                showError("请选择.np格式的文件");
                return;
            }
            
            // 创建新标签页并在其中加载文件
            frame.getTabManager().addNewTab(selectedFile.getName());
            EditorPane editor = getCurrentEditorFromFrame(frame);
            if (editor != null) {
                performOpen(editor, selectedFile);
            }
        }
    }
    
    private static void performOpen(EditorPane editor, File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            
            // 读取文件内容
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            // 设置编辑器内容
            editor.setText(content.toString());
            editor.setCurrentFile(file);
            editor.setModified(false);
            
            // 更新标签页标题为文件名
            updateTabTitle(editor, file);
        } catch (IOException e) {
            showError("读取失败: " + e.getMessage());
        }
    }
    
    private static void updateTabTitle(EditorPane editor, File file) {
        Component comp = editor;
        // 查找包含编辑器的 JTabbedPane
        while (comp != null && !(comp.getParent() instanceof JTabbedPane)) {
            comp = comp.getParent();
        }
        
        if (comp != null) {
            JTabbedPane pane = (JTabbedPane) comp.getParent();
            int index = pane.indexOfComponent(comp);
            if (index != -1) {
                // 使用文件名作为标签页标题
                pane.setTitleAt(index, file.getName());
                pane.setTabComponentAt(index, new com.notepad.view.TabHeader(pane, file.getName()));
            }
        }
    }

    private static EditorPane getCurrentEditorFromFrame(MainFrame frame) {
        try {
            JComponent tab = frame.getTabManager().getCurrentTab();
            if (tab instanceof JPanel) {
                JScrollPane scrollPane = (JScrollPane) ((JPanel) tab).getComponent(0);
                return (EditorPane) scrollPane.getViewport().getView();
            }
        } catch (Exception e) {
            showError("获取编辑器失败: " + e.getMessage());
        }
        return null;
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}