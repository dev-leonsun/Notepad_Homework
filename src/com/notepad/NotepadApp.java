package com.notepad;

import com.notepad.view.MainFrame;
import javax.swing.SwingUtilities;

/**
 * 程序启动入口
 */
public class NotepadApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}