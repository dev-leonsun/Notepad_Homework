package com.notepad.core;

import com.notepad.view.EditorPane;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FileController {
    private static final String FILE_EXTENSION = "np";
    private static final String FILE_DESCRIPTION = "Notepad文件 (*.np)";

    public static void saveFile(EditorPane editor, JFrame parent) {
        if (editor.getCurrentFile() == null) {
            saveAsFile(editor, parent);
        } else {
            performSave(editor, editor.getCurrentFile());
        }
    }

    public static void saveAsFile(EditorPane editor, JFrame parent) {
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
        }
    }

    private static void performSave(EditorPane editor, File file) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {

            editor.write(writer);
            editor.setCurrentFile(file);
            editor.setModified(false);
        } catch (IOException e) {
            showError("保存失败: " + e.getMessage());
        }
    }
    
    public static void openFile(EditorPane editor, JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        // 设置文件过滤器，只显示.np文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter(FILE_DESCRIPTION, FILE_EXTENSION);
        chooser.setFileFilter(filter);
        
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            performOpen(editor, selectedFile);
        }
    }
    
    private static void performOpen(EditorPane editor, File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            
            editor.read(reader, null);
            editor.setCurrentFile(file);
            editor.setModified(false);
            
            // 更新标签页标题为文件名
            updateTabTitle(editor, file);
        } catch (IOException e) {
            showError("读取失败: " + e.getMessage());
        }
    }
    
    private static void updateTabTitle(EditorPane editor, File file) {
        // 找到编辑器所在的标签页，并更新标题
        JTabbedPane pane = (JTabbedPane) editor.getParent().getParent().getParent();
        int index = pane.indexOfComponent(editor.getParent().getParent());
        if (index != -1) {
            // 使用文件名作为标签页标题
            pane.setTitleAt(index, file.getName());
            pane.setTabComponentAt(index, new com.notepad.view.TabHeader(pane, file.getName()));
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}