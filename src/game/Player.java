package game;

public class Player extends Trainer {

    @Override
    protected void chooseCell() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
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
