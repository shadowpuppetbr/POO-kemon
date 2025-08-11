package game;
import core.enums.PokeState;
import core.enums.PokeType;
import java.util.Random;
import javax.swing.ImageIcon;

public abstract class Pokemon {
    protected int hp;
    protected int level;
    protected int experience;
    protected PokeType type;
    protected int strength;
    protected String name;
    protected PokeState state;
    protected ImageIcon image;
    protected static final Random random = new Random();
    
    public Pokemon(String name) {
        this.name = name;
        this.strength = 10;
        this.level = 1;
        this.experience = 0;
        this.hp = 100;
        this.state = PokeState.WILD;
        
    }
    
    public abstract int attack();
    public void takeDamage(int value, PokeType region){
        if( region == PokeType.WATER){
            this.hp -= value;
        }
        else{
            this.hp -= (value * 0.7);
        }
        if(this.hp <= 0){
            this.hp = 0;
            this.state = PokeState.DISABLED;
        }
    }
    
   public void levelUp() {
       experience += 10;
        if(experience >= 100) {
            level++;
            experience = 0;
            strength += 5;
        }
    }

    protected void setImage(ImageIcon image) {
        this.image = image;
    }

    public ImageIcon getImage(){
        return this.image;
    }

    public String getName(){
        return this.name;
    }

    public void setPokeState(PokeState state) {
        this.state = state;
    }
    public PokeState getPokeState() {
        return this.state;
    }
    
    public PokeType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Pokemon [hp=" + hp + ", level=" + level + ", experience=" + experience + ", type=" + type
                + ", strength=" + strength + ", name=" + name + ", state=" + state + "]";
    }

    
}


class ForestPokemon extends Pokemon{

    public ForestPokemon(String name) {
        super(name);
        this.type = PokeType.FOREST;
    }
    @Override
    public int attack(){
        int damage = (random.nextInt(10) + this.strength)* this.level;
        this.hp = (int)(damage * 0.1);
        if(this.hp > 100){
            this.hp = 100;
        }
        return damage;
    }
    
}


class WaterPokemon extends Pokemon{

    public WaterPokemon(String name) {
        super(name);
        this.type = PokeType.WATER;
    }
    @Override
    public int attack(){
        int damage = (random.nextInt(10) + this.strength)* this.level;
        return damage;
    }
    
    
}


class GroundPokemon extends Pokemon{
    private int turn;

    public GroundPokemon(String name) {
        super(name);
        this.type = PokeType.GROUND;
    }
    @Override
    public int attack(){
        this.turn++;
        int damage;
        if(this.turn % 2 == 1){
            damage = (random.nextInt(10) + (2 * this.strength))* this.level;
        }
        else{
            damage = (random.nextInt(10) + this.strength)* this.level;
        }
        return damage;
    }
    
    
}


class ElectricPokemon extends Pokemon{

    public ElectricPokemon(String name) {
        super(name);
        this.type = PokeType.ELECTRIC;
    }
    @Override
    public int attack(){
        int damage = (random.nextInt(10) + this.strength)* this.level;
        return damage;
    }

    public boolean paralyze(){
        return random.nextInt(10) < 4;
    }

    
}
