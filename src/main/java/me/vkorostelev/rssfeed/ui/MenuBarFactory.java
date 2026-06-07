package me.vkorostelev.rssfeed.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuBarFactory {
    public static JMenuBar create(JFrame frame, Runnable onSettings, Runnable onOfflineMode, Runnable onSyncAll,
                                  Runnable onAddFolder, Runnable onAddFeed, Runnable onDeleteFeed,
                                  Runnable onDeleteFolder, Runnable onSyncFeed, Runnable onSyncFolder) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.add(createMenuItem("Добавить папку", onAddFolder));
        fileMenu.add(createMenuItem("Добавить ленту", onAddFeed));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Удалить ленту", onDeleteFeed));
        fileMenu.add(createMenuItem("Удалить папку", onDeleteFolder));
        menuBar.add(fileMenu);

        JMenu syncMenu = new JMenu("Синхронизация");
        syncMenu.add(createMenuItem("Синхронизировать все", onSyncAll));
        syncMenu.add(createMenuItem("Синхронизировать ленту", onSyncFeed));
        syncMenu.add(createMenuItem("Синхронизировать папку", onSyncFolder));
        menuBar.add(syncMenu);

        JMenu viewMenu = new JMenu("Вид");
        viewMenu.add(createMenuItem("Настройки", onSettings));
        viewMenu.add(createMenuItem("Оффлайн режим", onOfflineMode));
        menuBar.add(viewMenu);

        return menuBar;
    }

    private static JMenuItem createMenuItem(String text, Runnable action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(e -> action.run());
        return item;
    }
}
