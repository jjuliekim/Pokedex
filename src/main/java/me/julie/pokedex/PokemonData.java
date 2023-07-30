package me.julie.pokedex;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PokemonData {
    private int height;
    private URL image;
    private URL shiny;
    private int weight;
    private List<String> types;
    private String region;
    private List<String> strengths;
    private List<String> weaknesses;

    public PokemonData(int height, URL image, URL shiny, int weight,
                       List<String> type, String region, List<String> strengths, List<String> weaknesses) {
        this.height = height;
        this.image = image;
        this.shiny = shiny;
        this.weight = weight;
        this.types = type;
        this.region = region;
        this.strengths = strengths;
        this.weaknesses = weaknesses;
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
        return listToLabel(types);
    }

    public String getRegion() {
        return region;
    }

    public String getStrengths() {
        return listToLabel(strengths);
    }

    public String getWeaknesses() {
        return listToLabel(weaknesses);
    }

    public String listToLabel(List<String> list) {
        Set<String> seen = new HashSet<>();
        StringBuilder label = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (!seen.add(list.get(i))) {
                continue;
            }
            if (i != 0) {
                label.append(", ").append(list.get(i));
            } else {
                label.append(list.get(i));
            }
        }
        return label.toString();
    }
}
