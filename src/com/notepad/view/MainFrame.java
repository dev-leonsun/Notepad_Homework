package com.notepad.view;

import com.notepad.core.TabManager;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

/**
 * 主窗口框架
 * 职责：管理核心UI组件和顶级事件监听
 */
public class MainFrame extends JFrame {
    private TabManager tabManager;

    public MainFrame() {
        super("记事本");
        this.tabManager = tabManager;
        initWindowSettings();
        initComponents();
    }

    private void initWindowSettings() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // 窗口居中
    }

    private void initComponents() {
        // 初始化标签管理器
        tabManager = new TabManager(this);

        // 初始化菜单系统
        JMenuBar menuBar = new com.notepad.core.MenuManager(this).getMenuBar();
        setJMenuBar(menuBar);

        // 添加窗口关闭监听
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                tabManager.confirmExit();
            }
        });
    }

    public TabManager getTabManager() {
        return tabManager;
    }
}