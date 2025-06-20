package me.vkorostelev.jbrss.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.vkorostelev.jbrss.rss.RssMain;
import me.vkorostelev.jbrss.rss.RssStorage;
import me.vkorostelev.jbrss.rss.entity.FeedEntry;
import me.vkorostelev.jbrss.rss.entity.FeedFolder;
import me.vkorostelev.jbrss.util.AbstractUIView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Executors;

import static javafx.collections.FXCollections.observableArrayList;

@Slf4j
public class MainController extends AbstractUIView {
    private static final String NEW_RSS_FEED = "New RSS Feed";
    private static final String NEW_RSS_FOLDER = "New RSS Folder";

    private final RssMain rssMain = new RssMain();
    private final RssStorage storage = new RssStorage();

    @FXML
    public TableView<FeedEntry> tblRss;
    @FXML
    public TableColumn<FeedEntry, String> tcTitle;
    @FXML
    public TableColumn<FeedEntry, Date> tcPublished;
    @FXML
    public TreeView<FeedFolder> tvFolders;
    @FXML
    public WebView wvContent;

    private TreeItem<FeedFolder> treeRoot;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcTitle.setCellValueFactory(entry -> new SimpleObjectProperty<>(entry.getValue().getTitle()));
        tcPublished.setCellValueFactory(entry -> new SimpleObjectProperty<>(entry.getValue().getPublished()));

        buildTreeContextMenu();

        try {
            storage.load();
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                storage.setRootFolder(FeedFolder.builder().id(UUID.randomUUID()).childFolders(new ArrayList<>()).name("Root").feed(null).build());
                try {
                    storage.save();
                    storage.load();
                } catch (IOException ex) {
                    log.error("Can't save storage file", e);
                }
            } else {
                log.error("Can't load storage", e);
            }
        }

        buildTreeRoot();

        tvFolders.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {
                val selected = tvFolders.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if (tblRss.getItems() != null && !tblRss.getItems().isEmpty())
                        tblRss.getItems().clear();
                    if (selected.getValue().getFeed() != null)
                        tblRss.setItems(observableArrayList(selected.getValue().getFeed().getEntries()));
                }
            }
        });

        tblRss.setOnMouseClicked(e -> {
            if (e.getClickCount() > 2) {
                val selectedRow = tblRss.getSelectionModel().getSelectedItem();
                wvContent.getEngine().loadContent("");
                wvContent.getEngine().loadContent("");
            }
        });
    }

    private void buildTreeRoot() {
        treeRoot = new TreeItem<>(storage.getRootFolder());
        treeRoot.setExpanded(true);

        fillRssTree(treeRoot);

        tvFolders.setRoot(treeRoot);
    }

    private void fillRssTree(final TreeItem<FeedFolder> treeItem) {
        treeItem.getValue().getChildFolders().forEach(child -> {
            val childTreeItem = new TreeItem<>(child);
            childTreeItem.setExpanded(true);
            treeItem.getChildren().add(childTreeItem);
            fillRssTree(childTreeItem);
        });
    }

    public void onAddClick(ActionEvent actionEvent) {
        val txtDlg = new TextInputDialog("https://www.youtube.com/feeds/videos.xml?channel_id=UCClgnrIfArlWVWvaPc7KF7w");
        txtDlg.setTitle(NEW_RSS_FEED);
        txtDlg.setHeaderText(NEW_RSS_FEED);
        txtDlg.showAndWait().ifPresent(url -> {
            val folder = tvFolders.getSelectionModel().getSelectedItem().getValue();
            if (folder != null) {
                val feed = rssMain.loadFeed(url);
                storage.merge(feed, folder);
                if (tblRss.getItems() != null && !tblRss.getItems().isEmpty())
                    tblRss.getItems().clear();
                tblRss.setItems(observableArrayList(feed.getEntries()));
                storage.save();
            }
        });
    }

    public void onCloseClick(ActionEvent actionEvent) {
        currStage.close();
    }

    private void buildTreeContextMenu() {
        val setFeedMenuItem = new MenuItem("Set feed");
        val addFolderMenuItem = new MenuItem("Add folder");
        val refreshFeedMenuItem = new MenuItem("Refresh feed");
        val deleteFeedMenuItem = new MenuItem("Delete feed");
        tvFolders.setContextMenu(new ContextMenu(setFeedMenuItem, addFolderMenuItem, refreshFeedMenuItem, new SeparatorMenuItem(), deleteFeedMenuItem));

        setFeedMenuItem.setOnAction(this::onAddClick);

        addFolderMenuItem.setOnAction(e -> {
            val txtDlg = new TextInputDialog();
            txtDlg.setTitle(NEW_RSS_FOLDER);
            txtDlg.setHeaderText(NEW_RSS_FOLDER);
            val result = txtDlg.showAndWait();

            result.ifPresent(folderName -> {
                val selected = tvFolders.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    val newFolder = FeedFolder.builder()
                            .name(folderName)
                            .id(UUID.randomUUID())
                            .childFolders(new ArrayList<>())
                            .parentFolderId(selected.getValue().getId())
                            .build();
                    selected.getValue().getChildFolders().add(newFolder);
                    val childTreeItem = new TreeItem<>(newFolder);
                    selected.getChildren().add(childTreeItem);
                    storage.save();
                }
            });
        });

        deleteFeedMenuItem.setOnAction(e -> {
            val selected = tvFolders.getSelectionModel().getSelectedItem();
            if (selected != null) {
                deleteFolder(selected);
                try {
                    storage.load();
                } catch (IOException ex) {
                    log.error("Unable to load", ex);
                }
                buildTreeRoot();
            }
        });

        refreshFeedMenuItem.setOnAction(e -> {
            val selected = tvFolders.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getValue().getFeed() != null) {
                Executors.newFixedThreadPool(1).submit(() -> {
                    val newFeed = rssMain.loadFeed(selected.getValue().getFeed().getUrl());
                    log.info("Updating feed {}", selected.getValue().getFeed().getId());
                    storage.merge(newFeed, selected.getValue());
                });
            }
        });
    }

    private void deleteFolder(final TreeItem<FeedFolder> item) {
        for (val c : item.getChildren()) {
            deleteFolder(c);
        }
        storage.remove(item.getValue());
        item.getValue().setFeed(null);
        storage.save();
    }

}
