package me.vkorostelev.rssfeed.ui;

import me.vkorostelev.rssfeed.model.Folder;
import me.vkorostelev.rssfeed.model.Feed;
import me.vkorostelev.rssfeed.model.FeedItem;

import javax.swing.*;
import java.util.List;

public class MainPanel extends JPanel {
    private FeedTreePanel treePanel;
    private FeedTablePanel tablePanel;
    private FeedContentPanel contentPanel;

    public MainPanel() {
        setLayout(new BorderLayout());

        treePanel = new FeedTreePanel();
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        tablePanel = new FeedTablePanel();
        contentPanel = new FeedContentPanel();
        rightSplit.setTopComponent(new JScrollPane(tablePanel));
        rightSplit.setBottomComponent(contentPanel);
        rightSplit.setResizeWeight(0.3);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setLeftComponent(treePanel);
        mainSplit.setRightComponent(rightSplit);
        mainSplit.setResizeWeight(0.2);

        add(mainSplit, BorderLayout.CENTER);

        treePanel.tree.addTreeSelectionListener(e -> {
            Folder folder = treePanel.getSelectedFolder();
            if (folder != null) {
                tablePanel.updateTable(folder.getFeeds());
            } else {
                tablePanel.updateTable(List.of());
            }
            contentPanel.showContent(null);
        });

        tablePanel.table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Feed feed = tablePanel.getSelectedFeed();
                if (feed != null && !feed.getItems().isEmpty()) {
                    contentPanel.showContent(feed.getItems().get(0));
                } else {
                    contentPanel.showContent(null);
                }
            }
        });
    }

    public void updateTree(List<Folder> folders) {
        treePanel.updateTree(folders);
    }
    
    public FeedTreePanel getTreePanel() { return treePanel; }
    public FeedTablePanel getTablePanel() { return tablePanel; }
}
