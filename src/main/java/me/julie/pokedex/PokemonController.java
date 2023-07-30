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
            if (currentPokemon == null) {
                currentPokemon = pokemonList.get(index);
            }
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
        strengthLabel.setText(pokemon.getStrengths());
        weaknessLabel.setText(pokemon.getWeaknesses());
        region.setText(pokemon.getRegion());
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
        final List<String> strengths = new ArrayList<>();
        final List<String> weaknesses = new ArrayList<>();
        for (final JsonNode typeNode : typesNode) {
            final JsonNode type = typeNode.get("type");
            types.add(type.get("name").asText());

            final URL typeURL = new URL(type.get("url").asText());
            final String jsonTypeText;
            try (final InputStream in = typeURL.openStream()) {
                jsonTypeText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            final JsonNode typeURLNode = mapper.readTree(jsonTypeText);
            final JsonNode damageRelationsNode = typeURLNode.get("damage_relations");
            final JsonNode doubleDamageFromNode = damageRelationsNode.get("double_damage_from");
            for (final JsonNode doubleDamageFrom : doubleDamageFromNode) {
                weaknesses.add(doubleDamageFrom.get("name").asText());
            }
            final JsonNode doubleDamageToNode = damageRelationsNode.get("double_damage_to");
            for (final JsonNode doubleDamageTo : doubleDamageToNode) {
                strengths.add(doubleDamageTo.get("name").asText());
            }
        }

        if (first) {
            evolutionChoice.getItems().clear();
            final URL speciesURL = new URL(node.get("species").get("url").asText());
            final String jsonSpeciesText;
            try (final InputStream in = speciesURL.openStream()) {
                jsonSpeciesText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            final JsonNode speciesNode = mapper.readTree(jsonSpeciesText);
            final URL evolURL = new URL(speciesNode.get("evolution_chain").get("url").asText());
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

        final URL genURL = new URL("https://pokeapi.co/api/v2/generation/" + Main.getInstance().getGeneration());
        final String jsonGenText;
        try (final InputStream in = genURL.openStream()) {
            jsonGenText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        final JsonNode generationNode = mapper.readTree(jsonGenText);
        final JsonNode mainRegionNode = generationNode.get("main_region");
        String pokemonRegion = mainRegionNode.get("name").asText();
        return new PokemonData(height, frontDefaultURL, frontShinyURL, weight,
                types, pokemonRegion, strengths, weaknesses);
    }
}
