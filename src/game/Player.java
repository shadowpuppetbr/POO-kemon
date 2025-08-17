package game;

public class Player extends Trainer {
    private static final long serialVersionUID = 1L;
    
    
    public static void main(String[] args) {
        Player player = new Player();
        Pokemon pokemon = PokemonFactory.createPokemon("Psyduck");
        System.out.println(pokemon.type);

        for(int i = 0; i < 10; i++){
            player.capturePokemon(pokemon);
        }
        
        for(Pokemon poke: player.team){
            System.out.println(poke.name);
        }
    }
    
}