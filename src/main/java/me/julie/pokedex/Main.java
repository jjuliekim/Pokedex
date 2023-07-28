package me.julie.pokedex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Main instance;
    private Stage mainStage;
    private int generation;

    @Override
    public void start(Stage stage) {
        instance = this;
        mainStage = stage;
        mainStage.setTitle("Starter Pokedex");
        loadMenu();
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Main getInstance() {
        return instance;
    }

    public Stage getStage() {
        return mainStage;
    }

    private void loadFXML(String fxmlFile) {
        try {
            final Scene scene = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource(fxmlFile + ".fxml")));
            mainStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMenu() {
        loadFXML("menu");
    }

    public void loadGen(int gen) {
        generation = gen;
        loadFXML("pokedex");
    }

    public int getGeneration() {
        return generation;
    }
}