package me.vkorostelev.rssfeed.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.vkorostelev.rssfeed.model.Folder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StorageManager {
    private final Path filePath;
    private final Gson gson;
    private final Type folderListType = new TypeToken<List<Folder>>() {}.getType();

    public StorageManager(Path filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List<Folder> load() throws IOException {
        if (!Files.exists(filePath)) {
            return List.of();
        }
        String json = Files.readString(filePath);
        return gson.fromJson(json, folderListType);
    }

    public void save(List<Folder> folders) throws IOException {
        Files.writeString(filePath, gson.toJson(folders));
    }
}
