package game;

import core.enums.PokeType;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Cell extends JButton{
    private Pokemon pokemon;
    private final PokeType regionType;
    private boolean isEmpty = true;
    private String message;
    private final Image[] tiles;

    /*
    Célula vazia parâmetro: RegionType argumento
    */
    public Cell(PokeType regionType) {
        super("");
        this.regionType = regionType;

        this.tiles = new Image[4];
        this.tiles[0] = new ImageIcon("src/resources/images/tile_water.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        this.tiles[1] = new ImageIcon("src/resources/images/tile_earth.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        this.tiles[2] = new ImageIcon("src/resources/images/tile_electric.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        this.tiles[3] = new ImageIcon("src/resources/images/tile_forest.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);

        setRegion();

    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.isEmpty = (pokemon == null);
        if (pokemon != null) {
            setIcon(pokemon.getImage());
        }
    }

    public void setMessage(String message) {
        this.message = message;
        if (message != null && !message.isEmpty()) {
            setText(message);
        }
    }


    private void setRegion(){

        switch (this.regionType) {
            case WATER -> {
                setBackground(new Color(106, 195, 207));
                setIcon(new ImageIcon(this.tiles[0]));
            }
            case GROUND -> {
                setBackground(new Color(120, 80, 40));
                setIcon(new ImageIcon(this.tiles[1]));
            }
            case ELECTRIC -> {
                setBackground(new Color(248, 224, 40));
                setIcon(new ImageIcon(this.tiles[2]));
            }
            case FOREST -> {
                setBackground(new Color(176, 248, 128));
                setIcon(new ImageIcon(this.tiles[3]));
            }
            default -> {
            } 
            }
        }

    

    // Getters for completeness
    public Pokemon getPokemon() {
        return pokemon;
    }

    public String getMessage() {
        return message;
    }

    public PokeType getRegionType() {
        return regionType;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

}
