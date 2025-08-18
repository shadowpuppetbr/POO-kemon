package game;

import javax.swing.ImageIcon;

public class PokemonFactory {

    public static Pokemon createPokemon(String name) {
        Pokemon pokemon;
        // Capitaliza a primeira letra para o nome do Pokémon
        String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);

        switch (name.toLowerCase()) {
            case "psyduck", "wartortle" -> pokemon = new WaterPokemon(capitalizedName);
            case "sandshrew", "sandslash" -> pokemon = new GroundPokemon(capitalizedName);
            case "pikachu", "raichu" -> pokemon = new ElectricPokemon(capitalizedName);
            case "bulbassauro", "caterpie" -> pokemon = new ForestPokemon(capitalizedName);
            default -> throw new IllegalArgumentException("Pokémon desconhecido: " + name);
        }

        // Define a imagem usando um caminho consistente baseado no nome em minúsculas
        String imagePath = "src/resources/images/pokemon/" + name.toLowerCase() + ".png";
        ImageIcon image = new ImageIcon(imagePath);
        pokemon.setImage(image);

        return pokemon;
    }
    public static void reinitializePokemonImage(Pokemon pokemon) {
        if (pokemon == null) return;
        
        String name = pokemon.getName();
        String imagePath = "src/resources/images/pokemon/" + name.toLowerCase() + ".png";
        ImageIcon image = new ImageIcon(imagePath);
        pokemon.setImage(image);
    }
}
