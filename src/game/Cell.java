package game;

import core.enums.PokeType;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public final class Cell extends JButton{
    private static final long serialVersionUID = 1L;
    private Pokemon pokemon;
    private final PokeType regionType;
    private String message;
    private transient final Image[] tiles;
    private boolean capturedFound;
    private final int[] coordinates;

    /*
    Célula vazia parâmetro: RegionType argumento
    */
    public Cell(PokeType regionType, int x, int y) {
        super("");
        this.regionType = regionType;
        this.capturedFound = false;
        this.coordinates = new int[2];
        this.coordinates[0] = x;
        this.coordinates[1] = y;

        this.tiles = new Image[4];
        this.tiles[0] = new ImageIcon("src/resources/images/tile_water.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        this.tiles[1] = new ImageIcon("src/resources/images/tile_earth.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        this.tiles[2] = new ImageIcon("src/resources/images/tile_electric.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        this.tiles[3] = new ImageIcon("src/resources/images/tile_forest.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);

        setRegion();

    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public void setMessage(String message) {
        this.message = message;
        if (message != null && !message.isEmpty()) {
            setText(message);
        }
    }


    public void setRegion(){

        switch (this.regionType) {
            case WATER -> {
                setBackground(new Color(106, 195, 207));
                setIcon(new ImageIcon(this.tiles[0]));
                setDisabledIcon(new ImageIcon(this.tiles[0]));
            }
            case GROUND -> {
                setBackground(new Color(120, 80, 40));
                setIcon(new ImageIcon(this.tiles[1]));
                setDisabledIcon(new ImageIcon(this.tiles[1]));
            }
            case ELECTRIC -> {
                setBackground(new Color(248, 224, 40));
                setIcon(new ImageIcon(this.tiles[2]));
                setDisabledIcon(new ImageIcon(this.tiles[2]));
            }
            case FOREST -> {
                setBackground(new Color(176, 248, 128));
                setIcon(new ImageIcon(this.tiles[3]));
                setDisabledIcon(new ImageIcon(this.tiles[3]));
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

    public int[] getCoordinates(){
        return coordinates;
    }

    public boolean isEmpty() {
        return pokemon == null;
    }

    public boolean isFound() {
        return capturedFound;
    }

    public void setFound(boolean found) {
        this.capturedFound = found;
        if(found){
            setEnabled(false);
            setIcon(pokemon.getImage());
            setDisabledIcon(pokemon.getImage());
        } else{
            setEnabled(true);
            setRegion();
        }
    }

}