package com.notepad.core;

import com.notepad.view.EditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 标签页管理器
 * 职责：处理多文档标签页的创建、关闭和状态管理
 */
public class TabManager {
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JFrame parent;
    public javax.swing.JComponent getCurrentTab() {
        return (javax.swing.JComponent) tabbedPane.getSelectedComponent();
    }

    public TabManager(JFrame parent) {
        this.parent = parent;
        parent.add(tabbedPane);
        addNewTab("未命名");
    }

    /**
     * 添加新标签页
     * @param title 标签页初始标题
     */
    public void addNewTab(String title) {
        EditorPane editorPane = new EditorPane();
        JPanel tabPanel = createTabPanel(editorPane);

        tabbedPane.addTab(title, tabPanel);
        int tabIndex = tabbedPane.getTabCount() - 1;
        tabbedPane.setTabComponentAt(tabIndex, new com.notepad.view.TabHeader(tabbedPane, title));
    }

    private JPanel createTabPanel(EditorPane editorPane) {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        panel.add(new javax.swing.JScrollPane(editorPane), java.awt.BorderLayout.CENTER);
        return panel;
    }

    /**
     * 退出确认流程
     */
    public void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
                parent,
                "确定要退出吗？未保存的更改将会丢失！",
                "退出确认",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}