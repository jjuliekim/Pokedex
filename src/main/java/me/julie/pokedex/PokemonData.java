package me.julie.pokedex;

import java.net.URL;

public class PokemonData {
    private int height;
    private URL image;
    private URL shiny;

    public PokemonData(int height, URL image, URL shiny) {
        this.height = height;
        this.image = image;
        this.shiny = shiny;
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
}
