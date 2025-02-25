package com.notepad.util;

import com.notepad.view.EditorPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 搜索和替换对话框
 * 提供搜索文本和替换功能
 */
public class SearchDialog extends JDialog {
    private JTextField searchField;
    private JTextField replaceField;
    private JButton findNextButton;
    private JButton replaceButton;
    private JButton replaceAllButton;
    private JButton cancelButton;
    private JCheckBox matchCaseCheckBox;
    
    private final EditorPane editor;
    private String lastSearchText = "";
    private int lastSearchPosition = 0;
    private boolean isReplaceMode = false;

    public SearchDialog(JFrame parent, EditorPane editor) {
        this(parent, editor, false);
    }
    
    public SearchDialog(JFrame parent, EditorPane editor, boolean isReplaceMode) {
        super(parent, isReplaceMode ? "替换" : "查找", false);
        this.editor = editor;
        this.isReplaceMode = isReplaceMode;
        initComponents();
        setLayout();
        addListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        searchField = new JTextField(20);
        
        if (isReplaceMode) {
            replaceField = new JTextField(20);
        }
        
        findNextButton = new JButton("查找下一个");
        
        if (isReplaceMode) {
            replaceButton = new JButton("替换");
            replaceAllButton = new JButton("全部替换");
        }
        
        cancelButton = new JButton("取消");
        matchCaseCheckBox = new JCheckBox("区分大小写");
    }

    private void setLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 输入区域面板
        JPanel inputPanel = new JPanel(new GridLayout(isReplaceMode ? 2 : 1, 2, 5, 5));
        
        // 查找内容行
        inputPanel.add(new JLabel("查找内容:", JLabel.RIGHT));
        inputPanel.add(searchField);
        
        // 替换内容行（仅在替换模式下显示）
        if (isReplaceMode) {
            inputPanel.add(new JLabel("替换为:", JLabel.RIGHT));
            inputPanel.add(replaceField);
        }
        
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // 选项区域
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.add(matchCaseCheckBox);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // 按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(findNextButton);
        
        if (isReplaceMode) {
            buttonPanel.add(replaceButton);
            buttonPanel.add(replaceAllButton);
        }
        
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void addListeners() {
        // 查找下一个按钮
        findNextButton.addActionListener(e -> findNext());

        // 替换按钮（仅在替换模式下）
        if (isReplaceMode) {
            replaceButton.addActionListener(e -> replace());
            replaceAllButton.addActionListener(e -> replaceAll());
        }

        // 取消按钮
        cancelButton.addActionListener(e -> setVisible(false));

        // 回车键触发查找
        searchField.addActionListener(e -> findNext());
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
     * @return 是否找到匹配项
     */
    private boolean findNext() {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            return false;
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
            return true;
        } else {
            // 从头开始搜索
            if (lastSearchPosition > 0) {
                int response = JOptionPane.showConfirmDialog(this,
                    "已到达文件末尾，是否从头开始搜索？",
                    "继续搜索",
                    JOptionPane.YES_NO_OPTION);
                    
                if (response == JOptionPane.YES_OPTION) {
                    lastSearchPosition = 0;
                    return findNext();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "找不到 \"" + searchText + "\"",
                    "搜索结果",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            return false;
        }
    }
    
    /**
     * 执行替换操作
     */
    private void replace() {
        // 检查是否有选中的文本
        String selectedText = editor.getSelectedText();
        String searchText = searchField.getText();
        
        if (selectedText != null && 
            (selectedText.equals(searchText) || 
            (!matchCaseCheckBox.isSelected() && selectedText.equalsIgnoreCase(searchText)))) {
            
            // 替换当前选中文本
            editor.replaceSelection(replaceField.getText());
            
            // 继续查找下一个
            findNext();
        } else {
            // 如果没有选中文本或选中文本不匹配，先查找
            findNext();
        }
    }
    
    /**
     * 执行全部替换操作
     */
    private void replaceAll() {
        String searchText = searchField.getText();
        String replaceText = replaceField.getText();
        
        if (searchText.isEmpty()) {
            return;
        }
        
        // 重置搜索位置
        lastSearchPosition = 0;
        
        int replacedCount = 0;
        
        // 保存原始文本，以支持撤销
        String originalText = editor.getText();
        
        // 执行替换
        if (matchCaseCheckBox.isSelected()) {
            // 区分大小写替换
            String newText = originalText.replace(searchText, replaceText);
            replacedCount = countOccurrences(originalText, searchText);
            editor.setText(newText);
        } else {
            // 不区分大小写替换（需要逐个查找和替换）
            StringBuilder newText = new StringBuilder(originalText);
            int offset = 0;
            
            while (lastSearchPosition >= 0 && lastSearchPosition < originalText.length()) {
                int foundPosition;
                String lowerCaseText = originalText.toLowerCase();
                String lowerCaseSearch = searchText.toLowerCase();
                
                foundPosition = lowerCaseText.indexOf(lowerCaseSearch, lastSearchPosition);
                
                if (foundPosition != -1) {
                    // 计算实际替换文本的位置（考虑到之前的替换可能改变了字符串长度）
                    int actualPosition = foundPosition + offset;
                    
                    // 替换文本
                    newText.replace(actualPosition, actualPosition + searchText.length(), replaceText);
                    
                    // 更新偏移量
                    offset += (replaceText.length() - searchText.length());
                    
                    // 更新下一个搜索位置
                    lastSearchPosition = foundPosition + searchText.length();
                    
                    replacedCount++;
                } else {
                    break;
                }
            }
            
            editor.setText(newText.toString());
        }
        
        // 显示替换结果
        if (replacedCount > 0) {
            JOptionPane.showMessageDialog(this, 
                "替换完成，共替换了 " + replacedCount + " 处。",
                "替换结果", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "没有找到需要替换的内容。",
                "替换结果",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        // 标记文档已修改
        editor.setModified(true);
    }
    
    /**
     * 计算字符串中特定子串出现的次数
     */
    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}