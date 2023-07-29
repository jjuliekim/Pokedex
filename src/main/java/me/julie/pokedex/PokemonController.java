package me.julie.pokedex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PokemonController {
    private List<String> pokemonList;
    private int index;
    private Scanner scanner;
    private boolean first;
    private String currentPokemon;
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
    private ChoiceBox<String> evolutionChoice;
    @FXML
    private Label weaknessLabel;
    @FXML
    private Label strengthLabel;
    @FXML
    private Label region;

    @FXML
    public void initialize() throws IOException {
        index = 0;
        first = true;
        pokemonList = new ArrayList<>();
        List<Button> buttons = new ArrayList<>();
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
            case 1 -> {
                pokemonList.addAll(Arrays.asList("Bulbasaur", "Charmander", "Squirtle"));
                region.setText("Kanto");
            }
            case 2 -> {
                pokemonList.addAll(Arrays.asList("Chikorita", "Cyndaquil", "Totodile"));
                region.setText("Johto");
            }
            case 3 -> {
                pokemonList.addAll(Arrays.asList("Treecko", "Torchic", "Mudkip"));
                region.setText("Hoenn");
            }
            case 4 -> {
                pokemonList.addAll(Arrays.asList("Turtwig", "Chimchar", "Piplup"));
                region.setText("Sinnoh");
            }
            case 5 -> {
                pokemonList.addAll(Arrays.asList("Snivy", "Tepig", "Oshawott"));
                region.setText("Unova");
            }
            case 6 -> {
                pokemonList.addAll(Arrays.asList("Chespin", "Fennekin", "Froakie"));
                region.setText("Kalos");
            }
            case 7 -> {
                pokemonList.addAll(Arrays.asList("Rowlet", "Litten", "Popplio"));
                region.setText("Alola");
            }
            case 8 -> {
                pokemonList.addAll(Arrays.asList("Grookey", "Scorbunny", "Sobble"));
                region.setText("Galar");
            }
            case 9 -> {
                pokemonList.addAll(Arrays.asList("Sprigatito", "Fuecoco", "Quaxly"));
                region.setText("Paldea");
            }
        }
        setButtonActions();
        currentPokemon = pokemonList.get(0);
        mapping();
    }

    private void setButtonActions() {
        backButton.setOnAction(e -> {
            Main.getInstance().loadMenu();
            first = true;
        });
        toggleShiny.setOnAction(e -> {
            if (toggleShiny.isSelected()) {
                try {
                    pokemonImage.setImage(new Image(getPokemonData(currentPokemon).getShinyImage().toString()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                try {
                    pokemonImage.setImage(new Image(getPokemonData(currentPokemon).getImage().toString()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        nextPokemon.setOnAction(e -> {
            index++;
            first = true;
            if (index == 3) {
                index = 0;
            }
            toggleShiny.setSelected(false);
            currentPokemon = pokemonList.get(index);
            try {
                mapping();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        prevPokemon.setOnAction(e -> {
            index--;
            first = true;
            if (index == -1) {
                index = 2;
            }
            toggleShiny.setSelected(false);
            currentPokemon = pokemonList.get(index);
            try {
                mapping();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        evolutionChoice.setOnAction(e -> {
            currentPokemon = evolutionChoice.getValue();
            try {
                mapping();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void mapping() throws IOException {
        PokemonData pokemon = getPokemonData(currentPokemon);
        pokemonImage.setImage(new Image(pokemon.getImage().toString()));
        nameLabel.setText(pokemonList.get(index));
        heightLabel.setText(pokemon.getHeight() * 10 + " cm / "
                + Math.round(pokemon.getHeight() * 3.937) + " in");
        weightLabel.setText(pokemon.getWeight() / 10 + " kg / "
                + Math.round(pokemon.getWeight() / 4.536) + " lbs");
        typeLabel.setText("[" + pokemon.getType() + "]");
        if ((pokemon.getType()).contains("grass")) {
            strengthLabel.setText("Water, ground, rock");
            weaknessLabel.setText("Fire, ice, poison, flying, bug");
        }
        if ((pokemon.getType()).contains("fire")) {
            strengthLabel.setText("Grass, ice, bug, steel");
            weaknessLabel.setText("Water, ground, rock");
        }
        if ((pokemon.getType()).contains("water")) {
            strengthLabel.setText("Fire, ground, rock");
            weaknessLabel.setText("Grass, electric");
        }
        if ((pokemon.getType()).contains("poison")) {
            strengthLabel.setText("Water, ground, rock, fairy");
            weaknessLabel.setText("Fire, ice, flying, bug, ground, psychic");
        }
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

        final JsonNode typesNode = node.get("types");
        final List<String> types = new ArrayList<>();
        for (final JsonNode typeNode : typesNode) {
            final JsonNode type = typeNode.get("type");
            types.add(type.get("name").asText());
        }

        if (first) {
            evolutionChoice.getItems().clear();
            final URL evolURL = new URL("https://pokeapi.co/api/v2/"
                    + "evolution-chain/" + node.get("id").asText());
            final String jsonEvolText;
            try (final InputStream in = evolURL.openStream()) {
                jsonEvolText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            final JsonNode evolNode = mapper.readTree(jsonEvolText);
            final JsonNode chainNode = evolNode.get("chain");
            final String firstEvolution = chainNode.get("species").get("name").asText();
            final String secondEvolution = chainNode.get("evolves_to").get(0).get("species").get("name").asText();
            final String thirdEvolution = chainNode.get("evolves_to")
                    .get(0).get("evolves_to").get(0).get("species").get("name").asText();
            evolutionChoice.getItems().add(firstEvolution);
            evolutionChoice.getItems().add(secondEvolution);
            evolutionChoice.getItems().add(thirdEvolution);
            first = false;
        }

        return new PokemonData(height, frontDefaultURL, frontShinyURL, weight, types);
    }
}
