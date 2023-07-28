package me.julie.pokedex;

import java.net.URL;

public class PokemonData {
    private int height;
    private URL image;

    public PokemonData(int height, URL image) {
        this.height = height;
        this.image = image;
    }

    public int getHeight() {
        return height;
    }

    public URL getImage() {
        return image;
    }
}
