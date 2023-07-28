package me.julie.pokedex;

import java.net.URL;

public class PokemonData {
    private int height;
    private URL image;
    private URL shiny;
    private int weight;

    public PokemonData(int height, URL image, URL shiny, int weight) {
        this.height = height;
        this.image = image;
        this.shiny = shiny;
        this.weight = weight;
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
}
