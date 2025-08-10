package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import view.Screen;
import view.StarterPokemon;

public class Game {
    private final Board board;
    private final Screen screen;
    private final Player player;
    private final Bot bot;
    private ArrayList<Pokemon> wildPokemon;
    private final Random random = new Random();
    private static final String POKEMONS_FILE_PATH = "src/core/pokemons.txt";
    private boolean isPlayerTurn = true;

    public Game() {
        // Initialize the game screen which contains the board
        this.board = new Board();
        this.screen = new Screen(board);
        this.player = new Player();
        this.bot = new Bot();
        this.wildPokemon = new ArrayList<>();
        this.bot.setGame(this); // Dá ao bot uma referência ao jogo
    }
    
    public void startNewGame(){ 

        // Inicializa a tela do jogo
        screen.initializeScreen();

        // Creates an array of the pokemon that will be used in the game
        List<String> pokemonNames = null;

        try{
            pokemonNames = Files.readAllLines(Paths.get(POKEMONS_FILE_PATH));
        } catch(IOException e){
            JOptionPane.showMessageDialog(screen, "Não foi possível ler o arquivo de Pokémons: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if we can't load essential game data
        }

        for(String pokemonName : pokemonNames) {
            wildPokemon.add(PokemonFactory.createPokemon(pokemonName));
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

        int rand = random.nextInt(wildPokemon.size());
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
        startPlayerTurn();

        screen.getEndTurnButton().addActionListener(_ -> {
            endPlayerTurn(); // Ends player's turn
        });

    }

    private void startPlayerTurn() {
        isPlayerTurn = true;
        
        screen.getChangePokemonButton().setEnabled(true);
        screen.getExitButton().setEnabled(true);
        screen.getEndTurnButton().setEnabled(true);
    }

    private void endPlayerTurn() {
        isPlayerTurn = false;
        screen.getChangePokemonButton().setEnabled(false);
        screen.getExitButton().setEnabled(false);
        screen.getEndTurnButton().setEnabled(false);

        // Starts bot's turn in a thread
        new Thread(bot).start();
    }

    public void endBotTurn() {
        startPlayerTurn();
    }

    public void loadGame() {
        // TODO
        System.out.println("Carregando jogo salvo...");
    }


    public Screen getScreen() {
        return screen;
    }

}
