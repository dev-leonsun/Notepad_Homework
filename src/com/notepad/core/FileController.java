package com.notepad.core;

import com.notepad.view.EditorPane;
import javax.swing.*;
import java.io.*;

public class FileController {
    public static void saveFile(EditorPane editor, JFrame parent) {
        if (editor.getCurrentFile() == null) {
            saveAsFile(editor, parent);
        } else {
            performSave(editor, editor.getCurrentFile());
        }
    }

    public static void saveAsFile(EditorPane editor, JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            performSave(editor, chooser.getSelectedFile());
        }
    }

    private static void performSave(EditorPane editor, File file) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {

            editor.write(writer);
            editor.setModified(false);
        } catch (IOException e) {
            showError("保存失败: " + e.getMessage());
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}