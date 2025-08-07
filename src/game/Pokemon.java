package game;
import java.util.Random;

enum PokeType {
    WATER, EARTH, ELECTRIC, FOREST
}

enum PokeState {
    WILD, NORMAL, STUNNED, DISABLED
}



abstract class Pokemon {
    protected int hp;
    protected int level;
    protected int experience;
    protected PokeType type;
    protected int strength;
    protected String name;
    protected PokeState state;
    
    public Pokemon(String name, int strength, int level, PokeType type) {
        this.name = name;
        this.strength = strength;
        this.level = level;
        this.experience = 0;
        this.hp = 100;
        this.type = type;
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
    public ForestPokemon(String name, int strength, int level) {
        super(name, strength, level, PokeType.FOREST);
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
    public WaterPokemon(String name, int strength, int level) {
        super(name, strength, level, PokeType.WATER);
    }
    @Override
    public int attack(){
        int damage = (random.nextInt(10) + this.strength)* this.level;
        return damage;
    }
    
    
}

@SuppressWarnings("unused")
class EarthPokemon extends Pokemon{
    private int turn;
    Random random = new Random();
    public EarthPokemon(String name, int strength, int level) {
        super(name, strength, level, PokeType.EARTH);
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
    public ElectricPokemon(String name, int strength, int level) {
        super(name, strength, level, PokeType.ELECTRIC);
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
