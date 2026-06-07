package me.vkorostelev.rssfeed.ui;

import me.vkorostelev.rssfeed.model.Folder;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;

public class FeedTreePanel extends JPanel {
    JTree tree;
    private DefaultTreeModel treeModel;

    public FeedTreePanel() {
        tree = new JTree();
        add(new JScrollPane(tree));
    }

    public void updateTree(List<Folder> folders) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Корень");
        for (Folder folder : folders) {
            DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(folder.getName());
            folderNode.setUserObject(folder);
            root.add(folderNode);
        }
        treeModel = new DefaultTreeModel(root);
        tree.setModel(treeModel);
    }

    public Folder getSelectedFolder() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node != null && node.getUserObject() instanceof Folder) {
            return (Folder) node.getUserObject();
        }
        return null;
    }
}
