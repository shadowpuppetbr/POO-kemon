package game;

public class Player extends Trainer {

    @Override
    protected void chooseCell() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    public static void main(String[] args) {
        Player player = new Player();
        Pokemon pokemon = new EarthPokemon("Pokemon",10,1);
        
        for(int i = 0; i < 10; i++){
           player.capturePokemon(pokemon);
        }
        
        System.out.println(player.team);
    }
    
}
