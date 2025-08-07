package game;

import core.enums.PokeType;
import java.awt.*;
import javax.swing.JButton;

public class Cell extends JButton {
    private Pokemon pokemon;
    private final PokeType regionType;
    private boolean isEmpty = true;
    private String message;

    /*
    Célula vazia parâmetro: RegionType argumento
    */
    public Cell(PokeType regionType) {
        super("");
        this.regionType = regionType;

        setRegion(regionType);
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.isEmpty = (pokemon == null);
        if (pokemon != null) {
//            setText(pokemon.getName()); TODO make this method later
        } else {
            setText("");
        }
    }

    public void setMessage(String message) {
        this.message = message;
        if (message != null && !message.isEmpty()) {
            setText(message);
        }
    }

    private void setRegion(PokeType regionType) {
        switch (regionType) {
            case WATER -> setBackground(new Color(106, 195, 207));
            case EARTH -> setBackground(new Color(120, 80, 40));
            case ELECTRIC -> setBackground(new Color(248, 224, 40));
            case FOREST -> setBackground(new Color(176, 248, 128));
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
