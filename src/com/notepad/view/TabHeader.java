package com.notepad.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 自定义标签页头部组件
 * 包含标题和关闭按钮
 */
public class TabHeader extends JPanel {
    private final JTabbedPane pane;
    private final String title;

    public TabHeader(JTabbedPane pane, String title) {
        this.pane = pane;
        this.title = title;
        initComponents();
    }

    private void initComponents() {
        setOpaque(false);
        add(createTitleLabel());
        add(createCloseButton());
    }

    private javax.swing.JLabel createTitleLabel() {
        return new javax.swing.JLabel(title);
    }

    private JButton createCloseButton() {
        JButton closeBtn = new JButton("×");
        closeBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        closeBtn.setContentAreaFilled(false);

        // JDK 21新特性：模式匹配简化事件处理
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // 左键点击
                    closeTab();
                }
            }
        });
        return closeBtn;
    }

    private void closeTab() {
        int index = pane.indexOfTabComponent(this);
        if (index != -1) {
            pane.remove(index);
        }
    }
}