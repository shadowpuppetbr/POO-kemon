package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import view.Screen;
import view.StarterPokemon;

public class Game {
    private final Board board;
    private final Screen screen;
    private final Player player;
    private Pokemon[] pokemon;

    public Game() {
        // Initialize the game screen which contains the board
        this.board = new Board();
        this.screen = new Screen(board);
        this.player = new Player();
        this.pokemon = new Pokemon[8];
        
        
    }
    
    public void start(){ 

        // Inicializa a tela do jogo
        screen.initializeScreen();

        // Creates an array of the pokemon that will be used in the game
        List<String> pokemonString = new ArrayList<>();

        try{
            pokemonString = Files.readAllLines(Paths.get("src/core/pokemons.txt"));
        } catch(IOException e){
            System.out.println("Arquivo com pokemons não foi possível ser lido");
        }

        for(int i = 0; i < pokemonString.size(); i++) {
            pokemon[i] = PokemonFactory.createPokemon(pokemonString.get(i));
        }

        // Gets player's pokemon of choice
        StarterPokemon starterDialog = new StarterPokemon(screen, pokemon);
        starterDialog.setVisible(true);

        Pokemon chosenPokemon = starterDialog.getSelectedPokemon();

        if (chosenPokemon == null) {
            System.exit(0);
        }

        player.addPokemon(chosenPokemon);
        player.changePokemon(chosenPokemon);
        
        JOptionPane.showMessageDialog(screen, "Escolha uma célula no tabuleiro para posicionar seu Pokémon.", "Posicionar Pokémon", JOptionPane.INFORMATION_MESSAGE);
        board.letPlayerPlacePokemon(chosenPokemon, () -> {
            startGameLoop();
        });
    }
    
    private void startGameLoop() {
        System.out.println("jogo iniciado");
        // TO DO
    }

    public void start(boolean fromSave){
        if(fromSave == false){
            this.start();
            return;
        }

        System.out.println("começando do save");
        // TO DO
    }


    public Screen getScreen() {
        return screen;
    }

}
