package com.jbrss;

import com.jbrss.model.*;
import com.jbrss.service.RSSParser;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main {
    private DefaultMutableTreeNode rootNode;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea textArea;
    private DataManager dataManager = new DataManager();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("RSS Feed Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Tree setup
        rootNode = new DefaultMutableTreeNode(dataManager.getRoot());
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setPreferredSize(new Dimension(300, 700));

        // Right side container
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Top part: Table
        String[] columns = {"Title", "Link"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(650, 350));

        // Bottom part: Text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane textScroll = new JScrollPane(textArea);
        textScroll.setPreferredSize(new Dimension(650, 350));

        rightPanel.add(tableScroll, BorderLayout.NORTH);
        rightPanel.add(textScroll, BorderLayout.CENTER);

        // Main layout
        frame.setLayout(new BorderLayout());
        frame.add(treeScroll, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        setupContextMenu();
        setupListeners();

        frame.setVisible(true);
    }

    private void setupContextMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem addFolderItem = new JMenuItem("Add Folder");
        JMenuItem addFeedItem = new JMenuItem("Add Feed");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem refreshItem = new JMenuItem("Refresh");

        addFolderItem.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getUserObject() instanceof Folder) {
                String name = JOptionPane.showInputDialog("Enter folder name:");
                if (name != null && !name.isEmpty()) {
                    Folder parent = (Folder) selectedNode.getUserObject();
                    dataManager.addFolder(parent, name);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Folder(name));
                    treeModel.insertNode((DefaultMutableTreeNode) newNode, selectedNode, true);
                }
            }
        });

        addFeedItem.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getUserObject() instanceof Folder) {
                String title = JOptionPane.showInputDialog("Enter feed title:");
                String url = JOptionPane.showInputDialog("Enter feed URL:");
                if (title != null && url != null) {
                    try {
                        Folder folder = (Folder) selectedNode.getUserObject();
                        RSSFeed feed = RSSParser.fetchFeed(title, url);
                        dataManager.addFeed(folder, feed);
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(feed);
                        treeModel.insertNode((DefaultMutableTreeNode) newNode, selectedNode, true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error fetching feed: " + ex.getMessage());
                    }
                }
            }
        });

        deleteItem.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode != rootNode) {
                int index = tree.getSelectionModel().getIndex();
                treeModel.removeNodeFromParent(selectedNode);
            }
        });

        refreshItem.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getUserObject() instanceof RSSFeed) {
                RSSFeed feed = (RSSFeed) selectedNode.getUserObject();
                try {
                    RSSFeed updatedFeed = RSSParser.fetchFeed(feed.getTitle(), feed.getUrl());
                    feed.setItems(updatedFeed.getItems());
                    treeModel.nodeChanged(selectedNode);
                    updateTable(feed);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error refreshing feed: " + ex.getMessage());
                }
            }
        });

        popup.add(addFolderItem);
        popup.add(addFeedItem);
        popup.addSeparator();
        popup.add(deleteItem);
        popup.add(refreshItem);

        tree.setComponentPopupMenu(popup);
    }

    private void setupListeners() {
        // Tree selection listener
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getUserObject() instanceof RSSFeed) {
                RSSFeed feed = (RSSFeed) selectedNode.getUserObject();
                updateTable(feed);
            } else if (selectedNode != null && selectedNode.getUserObject() instanceof Folder) {
                // Clear table when a folder is selected but no specific feed
                tableModel.setRowCount(0);
            }
        });

        // Table selection listener
        table.getModel().addTableModelListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) tableModel.getValueAt(selectedRow, 0);
                String link = (String) tableModel.getValueAt(selectedRow, 1);
                textArea.setText("Title: " + title + "\nLink: " + link);
            }
        });
    }

    private void updateTable(RSSFeed feed) {
        tableModel.setRowCount(0);
        for (FeedItem item : feed.getItems()) {
            tableModel.addRow(new Object[]{item.getTitle(), item.getLink()});
        }
    }
}

