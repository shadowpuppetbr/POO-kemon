package game;
import core.enums.PokeState;
import core.enums.PokeType;
import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;

public abstract class Pokemon implements Serializable, Attack{
    private static final long serialVersionUID = 1L;
    protected int hp;
    protected int level;
    protected int experience;
    private int expThreshold;
    protected PokeType type;
    protected int strength;
    protected String name;
    protected PokeState state;
    protected transient ImageIcon image;
    protected static final Random random = new Random();
    
    public Pokemon(String name) {
        this.name = name;
        this.strength = 10;
        this.level = 1;
        this.experience = 0;
        this.expThreshold = 100;
        this.hp = 100;
        this.state = PokeState.WILD;
        
    }

    public void takeDamage(int value){
        this.hp -= value;
        
        if(this.hp <= 0){
            this.hp = 0;
        }
    }
    
   public void increaseXp() {
       experience += 100;
        if(experience >= expThreshold) {
            level++;
            experience = 0;
            strength += 5;
            expThreshold += 50;
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

    public int getHp() {
        return hp;
    }
    public void resetHp() {
        this.hp = 100;
        this.state = PokeState.NORMAL;
    }

    public int getLevel() {
        return this.level;
    }
   

    
}


class ForestPokemon extends Pokemon{
    private static final long serialVersionUID = 1L;

    public ForestPokemon(String name) {
        super(name);
        this.type = PokeType.FOREST;
    }

    @Override
    public int attack(){
        int damage = (random.nextInt(10) + this.strength)* this.level;
        this.hp = (int)(damage * 0.2);
        if(this.hp > 100){
            this.hp = 100;
        }
        return damage;
    }
    
}


class WaterPokemon extends Pokemon{
    private static final long serialVersionUID = 1L;

    public WaterPokemon(String name) {
        super(name);
        this.type = PokeType.WATER;
    }
    @Override
    public int attack(){
        int damage = (random.nextInt(10) + this.strength)* this.level;
        return damage;
    }

    @Override
    public void takeDamage(int value){
        super.takeDamage((int) (value * 0.7));
    }
    
    
}


class GroundPokemon extends Pokemon{
    private static final long serialVersionUID = 1L;
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
            damage = (random.nextInt(10) * 2 + this.strength)* this.level;
        }
        else{
            damage = (random.nextInt(10) + this.strength)* this.level;
        }
        return damage;
    }
    public void resetTurn(){
        this.turn = 0;
    }
    
    
}


class ElectricPokemon extends Pokemon{
    private static final long serialVersionUID = 1L;

    public ElectricPokemon(String name) {
        super(name);
        this.type = PokeType.ELECTRIC;
    }
    @Override
    public int attack(){
        int damage = (random.nextInt(10) + this.strength)* this.level;
        return damage;
    }

    public void paralyze(Pokemon enemyPokemon){
        if (random.nextInt(5) == 1){ // 1/4 chance
            enemyPokemon.setPokeState(PokeState.STUNNED);
        }
    }

    
}