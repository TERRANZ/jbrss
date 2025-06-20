package me.vkorostelev.jbrss;

import javafx.application.Application;
import javafx.stage.Stage;
import me.vkorostelev.jbrss.util.StageHelper;

import java.io.IOException;

public class JbRssApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageHelper.openWindow("w_main.fxml", "Main", true);
    }

    public static void main(String[] args) {
        launch();
    }
}