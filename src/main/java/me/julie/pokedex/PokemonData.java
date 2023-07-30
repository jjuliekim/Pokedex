package me.julie.pokedex;

import java.net.URL;
import java.util.List;

public class PokemonData {
    private int height;
    private URL image;
    private URL shiny;
    private int weight;
    private List<String> types;
    private String region;

    public PokemonData(int height, URL image, URL shiny, int weight, List<String> type, String region) {
        this.height = height;
        this.image = image;
        this.shiny = shiny;
        this.weight = weight;
        this.types = type;
        this.region = region;
    }

    public int getHeight() {
        return height;
    }

    public URL getImage() {
        return image;
    }

    public URL getShinyImage() {
        return shiny;
    }

    public int getWeight() {
        return weight;
    }

    public String getType() {
        StringBuilder typeLabel = new StringBuilder();
        for (int i = 0; i < types.size(); i++) {
            if (i < types.size() - 1) {
                typeLabel.append(types.get(i)).append(", ");
            } else {
                typeLabel.append(types.get(i));
            }
        }
        return typeLabel.toString();
    }

    public String getRegion() {
        return region;
    }
}
