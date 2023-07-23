package me.julie.pokedex;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL url = new URL("https://pokeapi.co/api/v2/pokemon/pikachu");
        String jsonText;
        try (InputStream in = url.openStream()) {
            jsonText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        PokemonJson pokemon = mapper.readValue(jsonText, PokemonJson.class);
        System.out.println("Pikachu is " + pokemon.getHeight() + " decimeters tall.");
    }

}