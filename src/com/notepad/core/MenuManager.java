package com.notepad.core;

import com.notepad.view.EditorPane;
import com.notepad.view.MainFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * 菜单系统管理器
 * 职责：构建和管理菜单栏及其事件
 */
public class MenuManager {
    private final JMenuBar menuBar = new JMenuBar();
    private final MainFrame frame;

    public MenuManager(MainFrame frame) {
        this.frame = frame;
        buildFileMenu();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void buildFileMenu() {
        JMenu fileMenu = new JMenu("文件");

        // 新建菜单项
        addMenuItem(fileMenu, "新建", KeyEvent.VK_N,
                e -> frame.getTabManager().addNewTab("未命名"));

        // 保存菜单项
        addMenuItem(fileMenu, "保存", KeyEvent.VK_S,
                e -> FileController.saveFile(getCurrentEditor(), frame));

        menuBar.add(fileMenu);
    }

    private EditorPane getCurrentEditor() {
        javax.swing.JComponent tab = frame.getTabManager().getCurrentTab();
        if (tab instanceof javax.swing.JPanel) {
            javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane) ((javax.swing.JPanel) tab).getComponent(0);
            return (EditorPane) scrollPane.getViewport().getView();
        }
        return null;
    }

    private void addMenuItem(JMenu menu, String text, int key, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setAccelerator(KeyStroke.getKeyStroke(key, InputEvent.CTRL_DOWN_MASK));
        item.addActionListener(action);
        menu.add(item);
    }
}