package me.julie.pokedex;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PokedexController {
    @FXML
    private VBox mainVBox;
    @FXML
    private Label starterLabel;
    @FXML
    private Label pokedexLabel;
    @FXML
    private Button kantoButton;

    @FXML
    public void initialize() {
        kantoButton.setFocusTraversable(false);
        kantoButton.setOnAction(e -> Main.getInstance().loadGen1());
    }

}