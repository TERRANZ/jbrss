package me.vkorostelev.rssfeed.ui;

import me.vkorostelev.rssfeed.model.Feed;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

public class FeedTablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<Feed> currentFeeds;

    public FeedTablePanel() {
        tableModel = new DefaultTableModel(new Object[]{"Название", "URL"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        add(new JScrollPane(table));
    }

    public void updateTable(List<Feed> feeds) {
        this.currentFeeds = feeds;
        tableModel.setRowCount(0);
        for (Feed feed : feeds) {
            tableModel.addRow(new Object[]{feed.getTitle(), feed.getUrl()});
        }
    }

    public Feed getSelectedFeed() {
        int row = table.getSelectedRow();
        if (row >= 0 && currentFeeds != null && row < currentFeeds.size()) {
            return currentFeeds.get(row);
        }
        return null;
    }
}
