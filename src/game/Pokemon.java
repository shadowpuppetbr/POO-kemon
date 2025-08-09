package game;
import core.enums.PokeState;
import core.enums.PokeType;
import java.util.Random;

abstract class Pokemon {
    protected int hp;
    protected int level;
    protected int experience;
    protected PokeType type;
    protected int strength;
    protected String name;
    protected PokeState state;
    
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

    
}


@SuppressWarnings("unused")
class ForestPokemon extends Pokemon{
    
    Random random = new Random();
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

@SuppressWarnings("unused")
class WaterPokemon extends Pokemon{
    Random random = new Random();
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

@SuppressWarnings("unused")
class GroundPokemon extends Pokemon{
    private int turn;
    Random random = new Random();
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

@SuppressWarnings("unused")
class ElectricPokemon extends Pokemon{
    Random random = new Random();
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
