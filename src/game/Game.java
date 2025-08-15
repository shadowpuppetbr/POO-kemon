package game;

import core.GameStarter;
import core.enums.PokeState;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

import view.OverlayFight;
import view.PokemonList;
import view.Screen;

public class Game {
    private final Board board;
    private final Screen screen;
    private final Player player;
    private final Bot bot;
    private final ArrayList<Pokemon> wildPokemon;
    private final Random random = new Random();
    private static final String POKEMONS_FILE_PATH = "src/core/pokemons.txt";
    private boolean botTurn = false;

    public Game() {
        // Initialize the game screen which contains the board
        this.board = new Board();
        this.screen = new Screen(board);
        this.player = new Player();
        this.bot = new Bot();
        this.wildPokemon = new ArrayList<>();
    }

    public void startNewGame() {

        // Initializes screen
        screen.initializeScreen();

        // Creates an array of the pokemon that will be used in the game
        List<String> pokemonNames;
        try {
            pokemonNames = Files.readAllLines(Paths.get(POKEMONS_FILE_PATH));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(screen, "Não foi possível ler o arquivo de Pokémons: " + e.getMessage(), "",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if we can't load essential game data
            return;
        }

        for (String pokemonName : pokemonNames) {
            wildPokemon.add(PokemonFactory.createPokemon(pokemonName));
        }

        // Gets player's pokemon of choice
        PokemonList starterDialog = new PokemonList(screen, wildPokemon, "Escolha seu Pokémon.");
        starterDialog.setVisible(true);

        Pokemon playerChosen = starterDialog.getSelectedPokemon();

        if (playerChosen == null) {
            System.exit(0);
        }

        wildPokemon.remove(playerChosen);
        playerChosen.setPokeState(PokeState.NORMAL);
        player.addPokemon(playerChosen);
        player.changePokemon(playerChosen);

        int rand = random.nextInt(wildPokemon.size());
        Pokemon botChosen = wildPokemon.get(rand);
        JOptionPane.showMessageDialog(screen, "Bot escolheu: " + botChosen.name, "", JOptionPane.INFORMATION_MESSAGE);// TODO: make this only on debug mode
        wildPokemon.remove(botChosen);
        botChosen.setPokeState(PokeState.NORMAL);
        bot.addPokemon(botChosen);
        bot.changePokemon(botChosen);

        ActionListener cellsListener = e -> {
            Cell clickedCell = (Cell) e.getSource();
            if (clickedCell.isEmpty()) {
                JOptionPane.showMessageDialog(screen, "sobrou nada", "", JOptionPane.INFORMATION_MESSAGE);
            } else if (clickedCell.getPokemon().getPokeState() == PokeState.WILD) { // Pokemon is wild, try to capture
                                                                                    // it
                boolean captured = player.capturePokemon(clickedCell.getPokemon());
                if (captured) {
                    JOptionPane.showMessageDialog(screen, "Pokémon capturado", "", JOptionPane.INFORMATION_MESSAGE);
                    clickedCell.setFound(true);
                    wildPokemon.remove(clickedCell.getPokemon());
                } else {
                    JOptionPane.showMessageDialog(screen, "Pokémon fugiu", "", JOptionPane.WARNING_MESSAGE);
                }
            } else if (clickedCell.getPokemon().getPokeState() == PokeState.NORMAL) { // Pokemon is own by bot, fight
                                                                                      // with him
                fight(player.mainPokemon, bot.mainPokemon);

            }
            board.disableCells();
        };

        JOptionPane.showMessageDialog(screen, "Escolha uma célula no tabuleiro para posicionar seu Pokémon.",
                "Posicionar Pokémon", JOptionPane.INFORMATION_MESSAGE);
        board.letPlayerPlacePokemon(playerChosen, () -> {
            board.letBotPlacePokemon(botChosen);
            board.addRandomPokemons(wildPokemon);
            board.addActionListenerToCells(cellsListener);
            startGameLoop();
        });

    }

    private void fight(Pokemon playerPokemon, Pokemon botPokemon) {
        OverlayFight overlay = new OverlayFight(screen, playerPokemon, botPokemon);
        overlay.setVisible(true);

        overlay.getBtnAttack().addActionListener(_ -> {
            // Turno do jogador
            int danoJogador = playerPokemon.attack();
            botPokemon.takeDamage(danoJogador, playerPokemon.getType());
            overlay.setHpBot(botPokemon.getHp());

            if (botPokemon.getHp() <= 0) {
                JOptionPane.showMessageDialog(screen, "Você venceu a batalha!", "Vitória",
                        JOptionPane.INFORMATION_MESSAGE);
                overlay.setVisible(false);
                playerPokemon.resetHp();
                botPokemon.resetHp();
                playerPokemon.increaseXp();
                endPlayerTurn();
                return;
            }

            // Turno do bot
            int danoBot = botPokemon.attack();
            playerPokemon.takeDamage(danoBot, botPokemon.getType());
            overlay.setHpPlayer(playerPokemon.getHp());

            if (playerPokemon.getHp() <= 0) {
                JOptionPane.showMessageDialog(screen, "Você perdeu a batalha!", "Derrota",
                        JOptionPane.INFORMATION_MESSAGE);
                overlay.setVisible(false);
                endPlayerTurn();
            }
        });

        overlay.getBtnRun().addActionListener(_ -> {
            JOptionPane.showMessageDialog(screen, "Você fugiu da batalha!", "Fuga", JOptionPane.INFORMATION_MESSAGE);
            overlay.setVisible(false);
            endPlayerTurn();
        });
    }

    private void startGameLoop() {
        bot.setGame(this);
        new Thread(bot).start();
        startPlayerTurn();

        screen.getEndTurnButton().addActionListener(_ -> {
            endPlayerTurn(); // Ends player's turn
        });
        screen.getChangePokemonButton().addActionListener(_ -> {
            PokemonList changeList = new PokemonList(screen, player.getTeam(), "Escolha o novo Pokémon principal.");
            changeList.setVisible(true);
            Pokemon newMain = changeList.getSelectedPokemon();
            if (newMain != null) {
                player.changePokemon(newMain);
            }
        });
        screen.getExitButton().addActionListener(_ -> {
            screen.dispose();
            GameStarter.main(null);
        });

    }

    private void startPlayerTurn() {
        screen.getChangePokemonButton().setEnabled(true);
        screen.getExitButton().setEnabled(true);
        screen.getEndTurnButton().setEnabled(true);
        board.enableCells();
    }

    private void endPlayerTurn() {
        screen.getChangePokemonButton().setEnabled(false);
        screen.getEndTurnButton().setEnabled(false);
        board.disableCells();
        if (this.wildPokemon.isEmpty()) {
            endGame();
            return;
        }
        screen.getExitButton().setEnabled(false);
        // Let the bot take its turn
        botTurn = true;

    }

    public void endBotTurn() {
        botTurn = false;
        startPlayerTurn();
    }

    private void endGame() {
        if (player.getScore() >= bot.getScore()) {
            JOptionPane.showMessageDialog(getScreen(), "Jogador venceu");
        } else {
            JOptionPane.showMessageDialog(getScreen(), "Bot venceu");
        }
    }

    public void loadGame() {
        // TODO
        System.out.println("Carregando jogo salvo...");
    }

    public Screen getScreen() {
        return screen;
    }

    public boolean isBotTurn() {
        return botTurn;
    }

    public Board getBoard() {
        return board;
    }
    public ArrayList<Pokemon> getWildPokemon() {
        return wildPokemon;
    }

    public void botFightsPlayer() {
        fight(player.mainPokemon, bot.mainPokemon);
    }

}
