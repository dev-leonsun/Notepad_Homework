package com.notepad.core;

import com.notepad.view.EditorPane;
import com.notepad.view.MainFrame;
import com.notepad.util.SearchDialog;  // 更新引用路径

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
    private SearchDialog searchDialog;

    public MenuManager(MainFrame frame) {
        this.frame = frame;
        buildFileMenu();
        buildEditMenu();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void buildFileMenu() {
        JMenu fileMenu = new JMenu("文件");

        // 新建菜单项
        addMenuItem(fileMenu, "新建", KeyEvent.VK_N,
                e -> frame.getTabManager().addNewTab("未命名"));
                
        // 打开菜单项
        addMenuItem(fileMenu, "打开", KeyEvent.VK_O,
                e -> FileController.openFile(frame));

        // 保存菜单项
        addMenuItem(fileMenu, "保存", KeyEvent.VK_S,
                e -> FileController.saveFile(getCurrentEditor(), frame));
                
        // 另存为菜单项
        addMenuItem(fileMenu, "另存为...", KeyEvent.VK_A,
                e -> FileController.saveAsFile(getCurrentEditor(), frame));

        fileMenu.addSeparator();
        
        // 退出菜单项
        addMenuItem(fileMenu, "退出", KeyEvent.VK_Q,
                e -> frame.getTabManager().confirmExit());

        menuBar.add(fileMenu);
    }
    
    private void buildEditMenu() {
        JMenu editMenu = new JMenu("编辑");
        
        // 添加常见的编辑功能
        addMenuItem(editMenu, "复制", KeyEvent.VK_C,
                e -> {
                    if (getCurrentEditor() != null) {
                        getCurrentEditor().copy();
                    }
                });
                
        addMenuItem(editMenu, "剪切", KeyEvent.VK_X,
                e -> {
                    if (getCurrentEditor() != null) {
                        getCurrentEditor().cut();
                    }
                });
                
        addMenuItem(editMenu, "粘贴", KeyEvent.VK_V,
                e -> {
                    if (getCurrentEditor() != null) {
                        getCurrentEditor().paste();
                    }
                });
                
        editMenu.addSeparator();
        
        // 添加搜索功能
        addMenuItem(editMenu, "查找", KeyEvent.VK_F,
                e -> {
                    EditorPane editor = getCurrentEditor();
                    if (editor != null) {
                        showSearchDialog(editor);
                    }
                });
                
        menuBar.add(editMenu);
    }
    
    /**
     * 显示搜索对话框
     * @param editor 当前活动的编辑器
     */
    private void showSearchDialog(EditorPane editor) {
        if (searchDialog == null) {
            searchDialog = new SearchDialog(frame, editor);
        } else {
            // 更新对话框关联的编辑器，因为可能切换了标签页
            searchDialog.dispose();
            searchDialog = new SearchDialog(frame, editor);
        }
        searchDialog.showDialog();
    }

    private EditorPane getCurrentEditor() {
        try {
            javax.swing.JComponent tab = frame.getTabManager().getCurrentTab();
            if (tab instanceof javax.swing.JPanel) {
                javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane) ((javax.swing.JPanel) tab).getComponent(0);
                return (EditorPane) scrollPane.getViewport().getView();
            }
        } catch (Exception e) {
            System.err.println("获取当前编辑器失败: " + e.getMessage());
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