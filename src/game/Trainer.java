package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public abstract class Trainer implements Serializable {
    private static final long serialVersionUID = 1L;
    protected ArrayList<Pokemon> team;
    private Pokemon mainPokemon;
    protected int score;
    
    public Trainer(){
        this.team = new ArrayList<>();
        this.score = 0;
    }
    
    protected abstract void chooseCell();
    
    protected void changePokemon(Pokemon pokemon){
        this.mainPokemon = pokemon;
    }

    protected void addPokemon(Pokemon pokemon) {
        this.team.add(pokemon);
    }
    
    protected boolean capturePokemon(Pokemon pokemon){
        if( new Random().nextBoolean() ){
            addScore(1);
            team.add(pokemon);
            return true;
        } else{
            return false;
        }
    }

    public ArrayList<Pokemon> getTeam(){
        return this.team;
    }

    public int getScore(){
        return this.score;
    }
    public Pokemon getMainPokemon(){
        return this.mainPokemon;
    }

    public void addScore(int val){
        if(val > 0){
            this.score += val;
        }
    }

    
}