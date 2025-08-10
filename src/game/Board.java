package game;

import core.enums.PokeType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Board extends JPanel {
    private static final int BOARD_SIZE = 10; // 10x10 board
    private Cell[][] cells;

    public Board() {
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        initializeBoard();
    }

    private void initializeBoard() {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];

        // Define the four sectors
        PokeType topLeftType = PokeType.WATER;
        PokeType topRightType = PokeType.GROUND;
        PokeType bottomLeftType = PokeType.ELECTRIC;
        PokeType bottomRightType = PokeType.FOREST;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                PokeType regionType;

                // Determine which sector the cell belongs to
                if (row < BOARD_SIZE / 2 && col < BOARD_SIZE / 2) {
                    // Top-left sector: WATER
                    regionType = topLeftType;
                } else if (row < BOARD_SIZE / 2 && col >= BOARD_SIZE / 2) {
                    // Top-right sector: GROUND
                    regionType = topRightType;
                } else if (row >= BOARD_SIZE / 2 && col < BOARD_SIZE / 2) {
                    // Bottom-left sector: ELETRIC
                    regionType = bottomLeftType;
                } else {
                    // Bottom-right sector: FOREST
                    regionType = bottomRightType;
                }

                cells[row][col] = new Cell(regionType);
                add(cells[row][col]);
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            return cells[row][col];
        }
        return null;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public void letPlayerPlacePokemon(Pokemon pokemon, Runnable onPlacementComplete) {
        // Uses one listener for all cells
        ActionListener placementListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell clickedCell = (Cell) e.getSource();

                if(clickedCell.getRegionType() != pokemon.getType()) {
                    JOptionPane.showMessageDialog(null, "Região do tipo " + clickedCell.getRegionType() + " incompatível com pokémon tipo " + pokemon.getType() + ".","Erro de Posicionamento", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                clickedCell.setPokemon(pokemon);
                clickedCell.setEnabled(false);
                
                // Remove all listeners after placement
                for(Cell[] cellLines: cells){
                    for(Cell cell: cellLines){
                        cell.removeActionListener(this);
                    }

                }
                
                // Callback
                onPlacementComplete.run();
            
            }
        };

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.addActionListener(placementListener);
            }
        }
    }

    public void letBotPlacePokemon(Pokemon pokemon) {
        while (true) {
            int row = (int) (Math.random() * BOARD_SIZE);
            int col = (int) (Math.random() * BOARD_SIZE);
            Cell cell = getCell(row, col);

            if (cell.getRegionType() == pokemon.getType() && cell.isEmpty()) {
                cell.setPokemon(pokemon);
                break;
            }
        }
    }

}
