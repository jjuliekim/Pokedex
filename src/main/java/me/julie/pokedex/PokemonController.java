package me.julie.pokedex;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokemonController {
    private List<String> pokemonList;
    @FXML
    private VBox mainVBox;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() throws IOException {
        pokemonList = new ArrayList<>();
        backButton.setOnAction(e -> Main.getInstance().loadMenu());
        switch (Main.getInstance().getGeneration()) {
            case 1:
                pokemonList.addAll(Arrays.asList("Bulbasaur", "Charmander", "Squirtle"));
                break;
            case 2:
                pokemonList.addAll(Arrays.asList("Chikorita", "Cyndaquil", "Totodile"));
                break;
            case 3:
                pokemonList.addAll(Arrays.asList("Treecko", "Torchic", "Mudkip"));
                break;
            case 4:
                pokemonList.addAll(Arrays.asList("Turtwig", "Chimchar", "Piplup"));
                break;
            case 5:
                pokemonList.addAll(Arrays.asList("Snivy, Tepig, Oshawott"));
                break;
            case 6:
                pokemonList.addAll(Arrays.asList("Chespin, Fennekin, Froakie"));
                break;
            case 7:
                pokemonList.addAll(Arrays.asList("Rowlet, Litten, Popplio"));
                break;
            case 8:
                pokemonList.addAll(Arrays.asList("Grookey, Scorbunny, Sobble"));
                break;
            case 9:
                pokemonList.addAll(Arrays.asList("Sprigatito, Fuecoco, Quaxly"));
                break;
        }
        mapping();
    }

    private void mapping() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i <= 2; i++) {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokemonList.get(i).toLowerCase());
            String jsonText;
            try (InputStream in = url.openStream()) {
                jsonText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            PokemonJson pokemon = mapper.readValue(jsonText, PokemonJson.class);
            System.out.println(pokemonList.get(i) + " is " + pokemon.getHeight() + " decimeters tall.");
        }
    }
}
