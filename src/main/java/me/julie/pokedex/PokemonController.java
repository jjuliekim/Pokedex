package me.julie.pokedex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PokemonController {
    private List<String> pokemonList;
    private List<String> images;
    private int index;
    private Scanner scanner;
    @FXML
    private VBox mainVBox;
    @FXML
    private Button backButton;
    @FXML
    private Button nextPokemon;
    @FXML
    private Button prevPokemon;
    @FXML
    private ImageView bgImage;
    @FXML
    private Label nameLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label funFactLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private ToggleButton toggleShiny;
    @FXML
    private ImageView pokemonImage;
    @FXML
    private GridPane gridPane;


    @FXML
    public void initialize() throws IOException {
        index = 0;
        pokemonList = new ArrayList<>();
        images = new ArrayList<>();
        scanner = new Scanner(Objects.requireNonNull(PokemonController.class.getClassLoader()
                .getResourceAsStream("funFacts.txt")));
        bgImage.fitWidthProperty().bind(mainVBox.widthProperty());
        bgImage.fitHeightProperty().bind(mainVBox.heightProperty());
        backButton.setOnAction(e -> Main.getInstance().loadMenu());
        backButton.setFocusTraversable(false);
        toggleShiny.setFocusTraversable(false);
        nextPokemon.setFocusTraversable(false);
        prevPokemon.setFocusTraversable(false);
        switch (Main.getInstance().getGeneration()) {
            case 1 -> pokemonList.addAll(Arrays.asList("Bulbasaur", "Charmander", "Squirtle"));
            case 2 -> pokemonList.addAll(Arrays.asList("Chikorita", "Cyndaquil", "Totodile"));
            case 3 -> pokemonList.addAll(Arrays.asList("Treecko", "Torchic", "Mudkip"));
            case 4 -> pokemonList.addAll(Arrays.asList("Turtwig", "Chimchar", "Piplup"));
            case 5 -> pokemonList.addAll(Arrays.asList("Snivy", "Tepig", "Oshawott"));
            case 6 -> pokemonList.addAll(Arrays.asList("Chespin", "Fennekin", "Froakie"));
            case 7 -> pokemonList.addAll(Arrays.asList("Rowlet", "Litten", "Popplio"));
            case 8 -> pokemonList.addAll(Arrays.asList("Grookey", "Scorbunny", "Sobble"));
            case 9 -> pokemonList.addAll(Arrays.asList("Sprigatito", "Fuecoco", "Quaxly"));
        }
        for (String pokemon : pokemonList) {
            images.add(pokemon + ".png");
        }
        nextPokemon.setOnAction(e -> {
            try {
                index++;
                if (index == 3) {
                    index = 0;
                }
                mapping();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        prevPokemon.setOnAction(e -> {
            try {
                index--;
                if (index == -1) {
                    index = 2;
                }
                mapping();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        prevPokemon.onMouseEnteredProperty()
                .set(e -> prevPokemon.setStyle("-fx-background-color: #faf6f6;"));
        prevPokemon.onMouseExitedProperty()
                .set(e -> prevPokemon.setStyle(""));
        nextPokemon.onMouseEnteredProperty().set(e -> nextPokemon.setStyle("-fx-background-color: #faf6f6;"));
        nextPokemon.onMouseExitedProperty().set(e -> nextPokemon.setStyle(""));
        backButton.onMouseEnteredProperty().set(e -> backButton.setStyle("-fx-background-color: #faf6f6;"));
        backButton.onMouseExitedProperty().set(e -> backButton.setStyle(""));
        run();
    }

    private void run() throws IOException {
        mapping();
    }

    private void mapping() throws IOException {
        PokemonData pokemon = getPokemonData(pokemonList.get(index));
        System.out.println(pokemonList.get(index) + " is " + pokemon.getHeight() + " decimeters tall.");
        pokemonImage.setImage(new Image(pokemon.getImage().toString()));
    }

    private PokemonData getPokemonData(final String pokemon) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokemon.toLowerCase());
        final String jsonText;
        try (final InputStream in = url.openStream()) {
            jsonText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        final JsonNode node = mapper.readTree(jsonText);
        final int height = node.get("height").asInt();
        final JsonNode spritesNode = node.get("sprites");
        final JsonNode otherNode = spritesNode.get("other");
        final JsonNode officialArtworkNode = otherNode.get("official-artwork");
        final URL frontDefaultURL = new URL(officialArtworkNode.get("front_default").asText());
        return new PokemonData(height, frontDefaultURL);
    }
}
