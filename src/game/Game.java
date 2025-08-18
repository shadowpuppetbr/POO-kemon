package game;

import core.GameSave;
import core.GameStarter;
import core.enums.PokeState;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import view.OverlayFight;
import view.PokemonList;
import view.Screen;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Board board;
    private transient Screen screen;
    private final Player player;
    private final Bot bot;
    private final ArrayList<Pokemon> wildPokemon;
    private final Random random = new Random();
    private static final String POKEMONS_FILE_PATH = "src/core/pokemons.txt";
    private boolean botTurn = false;
    private boolean hintMode = false;
    private boolean debugMode = false;
    private int hints;

    public Game() {
        // Initialize the game screen which contains the board
        this.board = new Board();
        this.screen = new Screen(board);
        this.player = new Player();
        this.bot = new Bot();
        this.wildPokemon = new ArrayList<>();
    }

    public void startNewGame() {

        hints = 3;
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
        wildPokemon.remove(botChosen);
        botChosen.setPokeState(PokeState.NORMAL);
        bot.addPokemon(botChosen);
        bot.changePokemon(botChosen);

        Object[] options = { "Escolher Posição", "Posição Aleatória" };
        int choice = JOptionPane.showOptionDialog(screen,
                "Como você deseja posicionar seu Pokémon inicial?",
                "Posicionar Pokémon",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {

            board.letPlayerPlacePokemon(playerChosen, () -> {
                board.letBotPlacePokemon(botChosen);
                board.addRandomPokemons(wildPokemon);
                setupActionListeners();
                screen.updateMainPokemon(playerChosen);
                startGameLoop();
            });
        }

        else {
            board.PlacePlayerPokemonRandomly(playerChosen);
            board.letBotPlacePokemon(botChosen);
            board.addRandomPokemons(wildPokemon);
            setupActionListeners();
            screen.updateMainPokemon(playerChosen);
            startGameLoop();
        }

    }

    private void flee(Cell cell) {
        int coords[] = cell.getCoordinates();
        int x = coords[0];
        int y = coords[1];
        int newX = -1, newY = -1;

        while ((newX < 0 || newY < 0) || (newX == x && newY == y) || // Is out of
                (newX >= board.getBoardSize() || newY >= board.getBoardSize()) || // bounds
                !board.getCell(newX, newY).isEmpty() || // Is empty
                board.getCell(newX, newY).getRegionType() != cell.getRegionType()) { // Wrong region
            newX = x + random.nextInt(3) - 1; // Can set new row to above or below
            newY = y + random.nextInt(3) - 1; // Can set new col to left or right
        }

        Cell newCell = board.getCell(newX, newY);
        newCell.setPokemon(cell.getPokemon());
        if (debugMode) {
            newCell.setIcon(newCell.getPokemon().getImage());
            newCell.setDisabledIcon(newCell.getPokemon().getImage());
        }

        cell.setFound(false);
        cell.setPokemon(null);

    }

    private void provideHint(Cell cell) {
        if (board.hasPokemonInRowOrColumn(cell)) {
            JOptionPane.showMessageDialog(screen, "Existe pelo menos um Pokémon nesta linha ou coluna!", "",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(screen, "Não há Pokémons nesta linha ou coluna.", "",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * @param playerPokemon Pokémon do player
     * @param botPokemon    Pokémon do bot
     * @param allowRun      true se quem iniciou pode fugir, false se não pode
     */
    private void fight(Pokemon playerPokemon, Pokemon botPokemon, boolean allowRun) {
        board.disableCells();

        OverlayFight overlay = new OverlayFight(screen, playerPokemon, botPokemon);
        overlay.setVisible(true);
        overlay.showTurnMessage("Turno do Jogador", 1200);

        // Só o player pode fugir (quem iniciou a batalha)
        // Só permite fugir se allowRun for true
        overlay.getBtnRun().setEnabled(allowRun);

        overlay.getBtnAttack().addActionListener(_ -> {
            // Desabilita o botão de ataque e de fugir durante a animação/ataque
            overlay.getBtnAttack().setEnabled(false);
            if (allowRun)
                overlay.getBtnRun().setEnabled(false);
            // Turno do jogador
            if (playerPokemon.getPokeState() == PokeState.STUNNED) {
                playerPokemon.setPokeState(PokeState.NORMAL); // Reseta o estado do Pokémon para normal após um turno
            } else {
                overlay.animateAttackPlayer();
                int danoJogador = playerPokemon.attack();
                if (playerPokemon instanceof ElectricPokemon && random.nextInt(5) <= 2) { // 20%
                    JOptionPane.showMessageDialog(screen, "Você paralizou o computador!", "Tzzzzz",
                            JOptionPane.INFORMATION_MESSAGE);
                    botPokemon.setPokeState(PokeState.STUNNED);
                }
                overlay.showDamage(danoJogador, true);
                botPokemon.takeDamage(danoJogador);
                overlay.setHpBot(botPokemon.getHp());
                overlay.flashBot();

                if (botPokemon.getHp() <= 0) { // jogador venceu
                    JOptionPane.showMessageDialog(screen, "Você venceu a batalha!", "Vitória",
                            JOptionPane.INFORMATION_MESSAGE);
                    overlay.setVisible(false);
                    playerPokemon.resetHp();
                    botPokemon.resetHp();
                    playerPokemon.increaseXp();
                    if (playerPokemon instanceof GroundPokemon) {
                        ((GroundPokemon) playerPokemon).resetTurn();
                    }
                    if (botPokemon instanceof GroundPokemon) {
                        ((GroundPokemon) playerPokemon).resetTurn();
                    }
                    player.addScore(1);
                    endPlayerTurn();
                    return;
                }
            }

            overlay.showTurnMessage("Turno do Computador", 1000);
            // Delay para ataque do bot
            new javax.swing.Timer(1500, evt -> {

                if (botPokemon.getPokeState() == PokeState.STUNNED) {
                    botPokemon.setPokeState(PokeState.NORMAL); // Reseta o estado do Pokémon para normal após um turno
                } else {
                    overlay.animateAttackBot();
                    int danoBot = botPokemon.attack();
                    if (botPokemon instanceof ElectricPokemon && random.nextInt(5) <= 2) { // 20% chance
                        JOptionPane.showMessageDialog(screen, "Bot te paralizou!", "Tzzzzz",
                                JOptionPane.INFORMATION_MESSAGE);
                        playerPokemon.setPokeState(PokeState.STUNNED);
                    }
                    overlay.showDamage(danoBot, false);
                    playerPokemon.takeDamage(danoBot);
                    overlay.setHpPlayer(playerPokemon.getHp());
                    overlay.flashPlayer();

                    if (playerPokemon.getHp() <= 0) {
                        JOptionPane.showMessageDialog(screen, "Você perdeu a batalha!", "Derrota",
                                JOptionPane.INFORMATION_MESSAGE);
                        overlay.setVisible(false);
                        if (playerPokemon instanceof GroundPokemon) {
                            ((GroundPokemon) playerPokemon).resetTurn();
                        }
                        if (botPokemon instanceof GroundPokemon) {
                            ((GroundPokemon) playerPokemon).resetTurn();
                        }
                        bot.addScore(1);
                        endPlayerTurn();
                    }
                }

                // Reabilita o botão de ataque após o ataque do bot
                overlay.getBtnAttack().setEnabled(true);
                // Só o player pode fugir, então reabilita apenas se for o turno dele
                // Só reabilita o botão de fugir se permitido
                if (allowRun)
                    overlay.getBtnRun().setEnabled(true);

                ((javax.swing.Timer) evt.getSource()).stop();
                overlay.showTurnMessage("Turno do Jogador", 1000);
            }).start();

        });

        overlay.getBtnRun().addActionListener(_ -> {
            if (!allowRun)
                return;

            Random random = new Random();
            if (random.nextInt(100) < 70) {
                JOptionPane.showMessageDialog(screen, "Você fugiu da batalha!", "Fuga",
                        JOptionPane.INFORMATION_MESSAGE);
                overlay.setVisible(false);
                if (playerPokemon instanceof GroundPokemon) {
                    ((GroundPokemon) playerPokemon).resetTurn();
                }
                if (botPokemon instanceof GroundPokemon) {
                    ((GroundPokemon) playerPokemon).resetTurn();
                }
                endPlayerTurn();
            } else {
                overlay.getBtnRun().setEnabled(false);
                JOptionPane.showMessageDialog(screen, "Você não conseguiu fugir!", "Falha",
                        JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

    private void setupActionListeners() {
        ActionListener cellsListener = e -> {
            Cell clickedCell = (Cell) e.getSource();

            if (hintMode) {
                provideHint(clickedCell);
                hintMode = false;
                return; // Action doesn't waste turn
            }

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
                    flee(clickedCell);
                }
            } else if (clickedCell.getPokemon().getPokeState() == PokeState.NORMAL) {
                // Pokemon is own by bot, fight with him
                // Player iniciou a batalha
                // Player iniciou a batalha, pode fugir
                fight(player.getMainPokemon(), clickedCell.getPokemon(), true);
                clickedCell.setFound(true);
            }
            board.disableCells();
        };
        board.addActionListenerToCells(cellsListener);

        screen.getChangePokemonButton().addActionListener(_ -> {
            PokemonList changeList = new PokemonList(screen, player.getTeam(), "Escolha o novo Pokémon principal.");
            changeList.setVisible(true);
            Pokemon newMain = changeList.getSelectedPokemon();
            if (newMain != null) {
                player.changePokemon(newMain);
                screen.updateMainPokemon(newMain);
            }
        });

        screen.getDebugButton().addActionListener(_ -> {
            debugMode = !debugMode; // invert debugMode

            board.debug(debugMode);
            if (debugMode) {
                JOptionPane.showMessageDialog(screen, "Bot escolheu: " + bot.getMainPokemon().getName(), "",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        screen.getHintButton().addActionListener(_ -> {
            hintMode = true;
            hints--;
            JOptionPane.showMessageDialog(screen,
                    "Modo Dica ativado. Clique em uma célula para verificar a linha e a coluna.", "Dica",
                    JOptionPane.INFORMATION_MESSAGE);

            if (hints <= 0) {
                screen.getHintButton().setEnabled(false);
            }
        });
        screen.getEndTurnButton().addActionListener(_ -> {
            endPlayerTurn(); // Ends player's turn
        });
        screen.getExitButton().addActionListener(_ -> {
            screen.dispose();
            GameStarter.main(null);
        });

        screen.getSaveButton().addActionListener(_ -> {
            GameSave.saveGame(this);
        });
    }

    private void startGameLoop() {
        bot.setGame(this);
        new Thread(bot).start();
        startPlayerTurn();
    }

    public void resumeGameAfterLoading() {
        this.screen = new Screen(this.board);
        this.screen.initializeScreen();
        setupActionListeners();
        this.board.updateBoardStateAfterLoading();
        startGameLoop();
    }

    private void startPlayerTurn() {
        screen.getDebugButton().setEnabled(true);
        screen.getSaveButton().setEnabled(true);
        screen.getChangePokemonButton().setEnabled(true);
        screen.getExitButton().setEnabled(true);
        screen.getEndTurnButton().setEnabled(true);
        if (hints > 0)
            screen.getHintButton().setEnabled(true);
        board.enableCells();
    }

    private void endPlayerTurn() {
        screen.updateScore(player.getScore());
        screen.updateMainPokemon(player.getMainPokemon());
        screen.getDebugButton().setEnabled(false);
        screen.getSaveButton().setEnabled(false);
        screen.getChangePokemonButton().setEnabled(false);
        screen.getEndTurnButton().setEnabled(false);
        screen.getHintButton().setEnabled(false);
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
        // Bot iniciou a batalha
        // Bot iniciou a batalha, player NÃO pode fugir
        fight(player.getMainPokemon(), bot.getMainPokemon(), false);
    }
}