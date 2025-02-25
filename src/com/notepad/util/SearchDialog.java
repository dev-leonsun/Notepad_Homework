package com.notepad.util;

import com.notepad.view.EditorPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 搜索对话框
 * 提供搜索文本功能
 */
public class SearchDialog extends JDialog {
    private JTextField searchField;
    private JButton findNextButton;
    private JButton cancelButton;
    private JCheckBox matchCaseCheckBox;
    
    private final EditorPane editor;
    private String lastSearchText = "";
    private int lastSearchPosition = 0;

    public SearchDialog(JFrame parent, EditorPane editor) {
        super(parent, "查找", false);
        this.editor = editor;
        initComponents();
        setLayout();
        addListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        searchField = new JTextField(20);
        findNextButton = new JButton("查找下一个");
        cancelButton = new JButton("取消");
        matchCaseCheckBox = new JCheckBox("区分大小写");
    }

    private void setLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 搜索输入区域
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("查找内容:"));
        inputPanel.add(searchField);
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // 选项区域
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.add(matchCaseCheckBox);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // 按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(findNextButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void addListeners() {
        // 查找下一个按钮
        findNextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });

        // 取消按钮
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        // 回车键触发查找
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });
    }

    /**
     * 显示对话框并设置焦点到搜索框
     */
    public void showDialog() {
        // 如果有选中的文本，自动填入搜索框
        String selectedText = editor.getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            searchField.setText(selectedText);
        }
        
        setVisible(true);
        searchField.requestFocus();
        searchField.selectAll();
        
        // 重置搜索位置
        lastSearchPosition = editor.getCaretPosition();
    }

    /**
     * 执行查找下一个操作
     */
    private void findNext() {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            return;
        }

        String text = editor.getText();
        boolean matchCase = matchCaseCheckBox.isSelected();
        
        // 如果搜索词改变或开启了新的搜索，重置起始位置
        if (!searchText.equals(lastSearchText)) {
            lastSearchPosition = 0;
            lastSearchText = searchText;
        }
        
        // 执行大小写敏感或不敏感的搜索
        int foundPosition;
        if (matchCase) {
            foundPosition = text.indexOf(searchText, lastSearchPosition);
        } else {
            foundPosition = text.toLowerCase().indexOf(searchText.toLowerCase(), lastSearchPosition);
        }
        
        if (foundPosition != -1) {
            // 找到匹配项，选中它
            editor.setCaretPosition(foundPosition);
            editor.select(foundPosition, foundPosition + searchText.length());
            editor.requestFocus();
            
            // 更新下一次搜索的起点
            lastSearchPosition = foundPosition + 1;
        } else {
            // 从头开始搜索
            if (lastSearchPosition > 0) {
                JOptionPane.showMessageDialog(this,
                    "已到达文件末尾，将从头开始搜索",
                    "继续搜索",
                    JOptionPane.INFORMATION_MESSAGE);
                lastSearchPosition = 0;
                findNext();
            } else {
                JOptionPane.showMessageDialog(this,
                    "找不到 \"" + searchText + "\"",
                    "搜索结果",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}