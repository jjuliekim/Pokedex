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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PokemonController {
    private List<String> pokemonList;
    private List<Button> buttons;
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
        buttons = new ArrayList<>();
        scanner = new Scanner(Objects.requireNonNull(PokemonController.class.getClassLoader()
                .getResourceAsStream("funFacts.txt")));
        bgImage.fitWidthProperty().bind(mainVBox.widthProperty());
        bgImage.fitHeightProperty().bind(mainVBox.heightProperty());
        toggleShiny.setFocusTraversable(false);
        buttons.addAll(Arrays.asList(prevPokemon, nextPokemon, backButton));
        buttons.forEach(button -> {
            button.setFocusTraversable(false);
            button.onMouseEnteredProperty().set(e -> button.setStyle("-fx-background-color: #faf6f6;"));
            button.onMouseExitedProperty().set(e -> button.setStyle(""));
        });
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
        setButtonActions();
        mapping();
    }

    private void setButtonActions() {
        backButton.setOnAction(e -> Main.getInstance().loadMenu());
        toggleShiny.setOnAction(e -> {
            if (toggleShiny.isSelected()) {
                try {
                    pokemonImage.setImage(new Image(getPokemonData(pokemonList.get(index)).getShinyImage().toString()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                try {
                    pokemonImage.setImage(new Image(getPokemonData(pokemonList.get(index)).getImage().toString()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        nextPokemon.setOnAction(e -> {
            index++;
            if (index == 3) {
                index = 0;
            }
            toggleShiny.setSelected(false);
            try {
                mapping();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        prevPokemon.setOnAction(e -> {
            index--;
            if (index == -1) {
                index = 2;
            }
            toggleShiny.setSelected(false);
            try {
                mapping();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void mapping() throws IOException {
        PokemonData pokemon = getPokemonData(pokemonList.get(index));
        pokemonImage.setImage(new Image(pokemon.getImage().toString()));
        nameLabel.setText(pokemonList.get(index));
        heightLabel.setText("Height: " + pokemon.getHeight() * 10 + " cm / "
                + Math.round(pokemon.getHeight() * 3.937) + " in");
        weightLabel.setText("Weight: " + pokemon.getWeight() / 10 + " kg / "
                + Math.round(pokemon.getWeight() / 4.536) + " lbs");
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

        final int weight = node.get("weight").asInt();

        final JsonNode spritesNode = node.get("sprites");
        final JsonNode otherNode = spritesNode.get("other");
        final JsonNode officialArtworkNode = otherNode.get("official-artwork");
        final URL frontDefaultURL = new URL(officialArtworkNode.get("front_default").asText());
        final URL frontShinyURL = new URL(officialArtworkNode.get("front_shiny").asText());

        return new PokemonData(height, frontDefaultURL, frontShinyURL, weight);
    }
}
