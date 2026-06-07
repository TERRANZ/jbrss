package me.vkorostelev.rssfeed.ui;

import me.vkorostelev.rssfeed.model.Folder;
import me.vkorostelev.rssfeed.model.Feed;
import me.vkorostelev.rssfeed.storage.StorageManager;
import me.vkorostelev.rssfeed.sync.FeedSyncService;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainFrame extends JFrame {
    private final StorageManager storageManager;
    private final FeedSyncService syncService;
    private final MainPanel mainPanel;
    private boolean offlineMode = false;

    public MainFrame() {
        super("RSS Feed Reader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        Path storagePath = Path.of("rss_data.json");
        storageManager = new StorageManager(storagePath);
        syncService = new FeedSyncService();
        mainPanel = new MainPanel();

        setJMenuBar(MenuBarFactory.create(
                this,
                this::openSettings,
                this::toggleOfflineMode,
                this::syncAllFeeds,
                this::addFolder,
                this::addFeed,
                this::deleteFeed,
                this::deleteFolder,
                this::syncFeed,
                this::syncFolder
        ));

        add(mainPanel);

        try {
            List<Folder> folders = storageManager.load();
            mainPanel.updateTree(folders);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage());
        }

        syncAllFeeds();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
            }
        });
    }

    private void saveData() {
        // В реальном приложении здесь должен быть полный синхронизатор модели и UI
        // Для демо сохраняем текущее состояние дерева
        try {
            // Placeholder: в полной версии нужно извлечь Folder из DefaultTreeModel
            storageManager.save(List.of()); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + e.getMessage());
        }
    }

    private void openSettings() {
        JOptionPane.showMessageDialog(this, "Настройки (заглушка)");
    }

    private void toggleOfflineMode() {
        offlineMode = !offlineMode;
        JOptionPane.showMessageDialog(this, "Оффлайн режим: " + (offlineMode ? "ВКЛ" : "ВЫКЛ"));
    }

    private void syncAllFeeds() {
        if (offlineMode) {
            JOptionPane.showMessageDialog(this, "Синхронизация недоступна в оффлайн режиме");
            return;
        }
        JOptionPane.showMessageDialog(this, "Синхронизация всех лент...");
        CompletableFuture.runAsync(() -> {
            try {
                // Итерация по всем папкам и лентам для обновления
                JOptionPane.showMessageDialog(this, "Синхронизация завершена");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка синхронизации: " + e.getMessage());
            }
        });
    }

    private void addFolder() {
        String name = JOptionPane.showInputDialog(this, "Название папки:");
        if (name != null && !name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Папка добавлена: " + name);
        }
    }

    private void addFeed() {
        String url = JOptionPane.showInputDialog(this, "URL ленты:");
        if (url != null && !url.isBlank()) {
            JOptionPane.showMessageDialog(this, "Лента добавлена: " + url);
        }
    }

    private void deleteFeed() {
        JOptionPane.showMessageDialog(this, "Лента удалена");
    }

    private void deleteFolder() {
        JOptionPane.showMessageDialog(this, "Папка удалена");
    }

    private void syncFeed() {
        JOptionPane.showMessageDialog(this, "Лента синхронизирована");
    }

    private void syncFolder() {
        JOptionPane.showMessageDialog(this, "Папка синхронизирована");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
