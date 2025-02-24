package com.notepad.view;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 富文本编辑器组件
 * 扩展功能：修改状态追踪、基础文本操作
 */
public class EditorPane extends JTextArea {
    private boolean isModified = false;
    private java.io.File currentFile;

    public EditorPane() {
        initEditorSettings();
        initDocumentListener();
    }
    // getter和setter方法
    public java.io.File getCurrentFile() {
        return currentFile;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    // 初始化编辑器样式
    private void initEditorSettings() {
        setFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 14));
        setTabSize(4); // 设置Tab为4空格
    }

    // 监听文档修改状态
    private void initDocumentListener() {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { markModified(); }
            @Override public void removeUpdate(DocumentEvent e) { markModified(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });
    }

    private void markModified() {
        if (!isModified) {
            isModified = true;
            updateTabTitle();
        }
    }

    private void updateTabTitle() {
        // 更新标签标题（例如添加*号标识未保存）
        javax.swing.JTabbedPane pane = (javax.swing.JTabbedPane) getParent().getParent().getParent();
        int index = pane.indexOfComponent(getParent().getParent());
        String title = pane.getTitleAt(index);

        if (!title.startsWith("*")) {
            pane.setTitleAt(index, "*" + title);
        }
    }
}