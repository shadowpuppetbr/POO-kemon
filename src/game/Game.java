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
    private final Bot bot;
    private ArrayList<Pokemon> wildPokemon;

    public Game() {
        // Initialize the game screen which contains the board
        this.board = new Board();
        this.screen = new Screen(board);
        this.player = new Player();
        this.bot = new Bot();
        this.wildPokemon = new ArrayList<>();
        
        
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
            wildPokemon.add(PokemonFactory.createPokemon(pokemonString.get(i)));
        }

        // Gets player's pokemon of choice
        StarterPokemon starterDialog = new StarterPokemon(screen, wildPokemon);
        starterDialog.setVisible(true);

        Pokemon playerChosen = starterDialog.getSelectedPokemon();

        if (playerChosen == null) {
            System.exit(0);
        }

        wildPokemon.remove(playerChosen);
        player.addPokemon(playerChosen);
        player.changePokemon(playerChosen);

        int rand = (int) (Math.random() * wildPokemon.size());
        Pokemon botChosen = wildPokemon.get(rand);
        wildPokemon.remove(botChosen);
        bot.addPokemon(botChosen);
        bot.changePokemon(botChosen); 

        JOptionPane.showMessageDialog(screen, "Escolha uma célula no tabuleiro para posicionar seu Pokémon.", "Posicionar Pokémon", JOptionPane.INFORMATION_MESSAGE);
        board.letPlayerPlacePokemon(playerChosen, () -> {
            board.letBotPlacePokemon(botChosen);
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
