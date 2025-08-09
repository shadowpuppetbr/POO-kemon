package game;

public class PokemonFactory {

    public static Pokemon createPokemon(String name) {
        switch (name.toLowerCase()) {
            case "psyduck" -> {
                return new WaterPokemon("Psyduck");
            }
            case "wartortle" -> {
                return new WaterPokemon("Wartortle");
            }
            case "sandshrew" -> {
                return new GroundPokemon("Sandshrew");
            }
            case "sandslash" -> {
                return new GroundPokemon("Sandslash");
            }
            case "pikachu" -> {
                return new ElectricPokemon("Pikachu");
            }
            case "raichu" -> {
                return new ElectricPokemon("Raichu");
            }
            case "bulbassauro" -> {
                return new ForestPokemon("Bulbassauro");
            }
            case "caterpie" -> {
                return new ForestPokemon("Caterpie");
            }

            default -> throw new IllegalArgumentException("Pokémon desconhecido: " + name);
        }
        
}

}