package me.vkorostelev.rssfeed.ui;

import me.vkorostelev.rssfeed.model.FeedItem;

import javax.swing.*;
import java.awt.*;

public class FeedContentPanel extends JPanel {
    private JTextPane contentPane;

    public FeedContentPanel() {
        setLayout(new BorderLayout());
        contentPane = new JTextPane();
        contentPane.setEditable(false);
        add(new JScrollPane(contentPane), BorderLayout.CENTER);
    }

    public void showContent(FeedItem item) {
        if (item == null) {
            contentPane.setText("Выберите элемент ленты");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<html><h1>").append(item.getTitle()).append("</h1>");
        sb.append("<p><b>Дата:</b> ").append(item.getPubDate()).append("</p>");
        sb.append("<p>").append(item.getContent()).append("</p>");
        sb.append("</html>");
        contentPane.setText(sb.toString());
    }
}
