package com.jbrss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("RSS Feed Reader");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            // Left side: Tree
            JTree tree = new JTree();
            JScrollPane treeScroll = new JScrollPane(tree);
            treeScroll.setPreferredSize(new Dimension(300, 700));

            // Right side container
            JPanel rightPanel = new JPanel(new BorderLayout());
            
            // Top part: Table
            JTable table = new JTable();
            JScrollPane tableScroll = new JScrollPane(table);
            tableScroll.setPreferredSize(new Dimension(650, 350));

            // Bottom part: Text area
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            JScrollPane textScroll = new JScrollPane(textArea);
            textScroll.setPreferredSize(new Dimension(650, 350));

            rightPanel.add(tableScroll, BorderLayout.NORTH);
            rightPanel.add(textScroll, BorderLayout.CENTER);

            // Main layout
            frame.setLayout(new BorderLayout());
            frame.add(treeScroll, BorderLayout.WEST);
            frame.add(rightPanel, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
