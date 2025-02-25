package com.notepad.view;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Component;

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

    public void setCurrentFile(java.io.File file) {
        this.currentFile = file;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        this.isModified = modified;
        updateTabTitle();
    }

    // 初始化编辑器样式
    private void initEditorSettings() {
        setFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 14));
        setTabSize(4); // 设置Tab为4空格
        setLineWrap(true); // 自动换行
        setWrapStyleWord(true); // 按单词换行
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
        try {
            Component comp = this;
            // 查找包含编辑器的 JTabbedPane
            while (comp != null && !(comp.getParent() instanceof javax.swing.JTabbedPane)) {
                comp = comp.getParent();
            }
            
            if (comp != null) {
                javax.swing.JTabbedPane pane = (javax.swing.JTabbedPane) comp.getParent();
                int index = pane.indexOfComponent(comp);
                
                if (index != -1) {
                    String title = pane.getTitleAt(index);
                    
                    // 如果是修改状态，添加星号
                    if (isModified && !title.startsWith("*")) {
                        pane.setTitleAt(index, "*" + title);
                    } else if (!isModified && title.startsWith("*")) {
                        pane.setTitleAt(index, title.substring(1));
                    }
                }
            }
        } catch (Exception e) {
            // 组件可能尚未添加到UI层次结构中
            System.err.println("更新标签页标题失败: " + e.getMessage());
        }
    }
}