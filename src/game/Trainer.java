
package game;
import java.util.ArrayList;
import java.util.Random;

public abstract class Trainer {
    protected ArrayList<Pokemon> team;
    protected Pokemon mainPokemon;
    protected int score;
    
    public Trainer(){
        this.team = new ArrayList<>();
        this.score = 0;
    }
    
    protected abstract void chooseCell();
    
    protected void changePokemon(Pokemon pokemon){
        this.mainPokemon = pokemon;
    }
    
    protected boolean capturePokemon(Pokemon pokemon){
        if( new Random().nextBoolean() ){
            team.add(pokemon);
            return true;
        } else{
            return false;
        }
    }
    
    
}
