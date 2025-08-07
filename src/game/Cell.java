package game;

import core.enums.PokeType;

import javax.swing.JButton;
import java.awt.*;

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
        if (regionType == PokeType.WATER) {
            setBackground(new Color(106, 195, 207));
        } else if (regionType == PokeType.EARTH) {
            setBackground(new Color(120, 80, 40));
        } else if (regionType == PokeType.ELETRIC) {
            setBackground(new Color(248, 224, 40));
        } else if (regionType == PokeType.FOREST) {
            setBackground(new Color(176, 248, 128));
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
