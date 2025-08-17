package game;

import core.InvalidRegionException;
import core.enums.PokeType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

                cells[row][col] = new Cell(regionType, row, col);
                add(cells[row][col]);
            }
        }
    }

    public void updateBoardStateAfterLoading() {
        removeAll();
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = cells[row][col];
                if (cell.isFound()) {
                    cell.setEnabled(false);
                }
                add(cell);
            }
        }
        revalidate();
        repaint();
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
                try{
                    if(clickedCell.getRegionType() != pokemon.getType()) {
                        throw new InvalidRegionException();
                    }
    
                    clickedCell.setPokemon(pokemon);
                    clickedCell.setFound(true);
                    
                    // Remove all listeners after placement
                    for(Cell[] row: cells){
                        for(Cell cell: row){
                            cell.removeActionListener(this);
                        }
                    }
                    
                    // Callback
                    onPlacementComplete.run();

                } catch (InvalidRegionException ex){
                    JOptionPane.showMessageDialog(null, "Região do tipo " + clickedCell.getRegionType() + 
                    " incompatível com pokémon tipo " + pokemon.getType() + ".","Erro de Posicionamento", JOptionPane.ERROR_MESSAGE);
                    
                }

            }
        };

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.addActionListener(placementListener);
            }
        }
    }

    public void PlacePlayerPokemonRandomly(Pokemon pokemon) {
        while (true) {
            int row = (int) (Math.random() * getBoardSize());
            int col = (int) (Math.random() * getBoardSize());
            Cell cell = getCell(row, col);

       
            if (cell.getRegionType() == pokemon.getType() && cell.isEmpty()) {
                cell.setPokemon(pokemon);
                cell.setFound(true); 
                break; 
            }
        }
    }

    public void letBotPlacePokemon(Pokemon pokemon) {
        while (true) {
            int row = (int) (Math.random() * BOARD_SIZE);
            int col = (int) (Math.random() * BOARD_SIZE);
            Cell cell = getCell(row, col);

            try{
                if (cell.getRegionType() != pokemon.getType()) throw new InvalidRegionException();

                if (cell.isEmpty()) {
                    cell.setPokemon(pokemon);
                    break;
                }

            } catch (InvalidRegionException ex){
            }

        }
    }

    public void disableCells() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setEnabled(false);
            }
        }
    }

    public void enableCells() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if(!cell.isFound()){
                    cell.setEnabled(true);
                }
            }
        }
    }

    public void addActionListenerToCells(ActionListener listener) {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.addActionListener(listener);
            }
        }
    }

    public void addRandomPokemons(ArrayList<Pokemon> wildPokemon) {
        for (Pokemon pokemon : wildPokemon) {
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

    public boolean hasPokemonInRowOrColumn(Cell cell) {
        int row = cell.getCoordinates()[0];
        int col = cell.getCoordinates()[1];
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (cells[row][i].getPokemon() != null) return true;
        }
        for(int i = 0; i < BOARD_SIZE; i++) {
            if(cells[i][col].getPokemon() != null) return true;
        }
        return false;
    }

    public void debug(boolean bool){
        if(bool){
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    Cell cell = getCell(row, col);
                    if (!cell.isEmpty()) {
                        cell.setIcon(cell.getPokemon().getImage());
                        cell.setDisabledIcon(cell.getPokemon().getImage());
                    }
                }
            }
            return;
        }
        for (int row = 0; row < BOARD_SIZE; row++) {
            for(int col = 0; col < BOARD_SIZE; col++){
                Cell cell = getCell(row, col);
                if(!cell.isFound()){
                    cell.setRegion();
                }
            }
        }
    }
}