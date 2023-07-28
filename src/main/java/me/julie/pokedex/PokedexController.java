package me.julie.pokedex;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PokedexController {
    @FXML
    private VBox mainVBox;
    @FXML
    private Label starterLabel;
    @FXML
    private Label pokedexLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView bgImage;

    @FXML
    public void initialize() {
        int gen = 1;
        bgImage.fitWidthProperty().bind(mainVBox.widthProperty());
        bgImage.fitHeightProperty().bind(mainVBox.heightProperty());
        for (int i = 0; i < gridPane.getColumnCount(); i++) {
            for (int j = 0; j < gridPane.getRowCount(); j++) {
                VBox vBox = new VBox();
                Button button = new Button("Gen" + gen);
                button.setFocusTraversable(false);
                int finalGen = gen;
                button.setOnAction(e -> Main.getInstance().loadGen(finalGen));
                button.setStyle("-fx-background-color: #c6caca; -fx-font-family: Candara; " +
                        "-fx-background-radius: 5px; -fx-font-size: 24px; -fx-text-fill: #1f2333");
                vBox.setAlignment(Pos.CENTER);
                vBox.getChildren().add(button);
                gridPane.add(vBox, i, j);
                gen++;
            }
        }
    }

}