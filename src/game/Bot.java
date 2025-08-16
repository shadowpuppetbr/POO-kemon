package game;

import core.enums.PokeState;
import java.awt.FlowLayout;
import java.io.Serializable;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import view.Screen;

public class Bot extends Trainer implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private transient Game game;
    private volatile boolean running = false;

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    protected void chooseCell() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (game.isBotTurn()) {
                Screen screen = game.getScreen();
                Board board = game.getBoard();
                int BOARD_SIZE = board.getBoardSize();

                JDialog botTurnMsg = new JDialog(screen, "Turno do Bot", false);

                botTurnMsg.setSize(300, 100);
                botTurnMsg.setLocationRelativeTo(screen);
                botTurnMsg.setLayout(new FlowLayout());
                botTurnMsg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                botTurnMsg.add(new JLabel("let bro cook..."));
                botTurnMsg.setVisible(true);

                try {
                    Thread.sleep(1500); // Waits 1.5 seconds
                } catch (InterruptedException e) {
                    running = false;
                    Thread.currentThread().interrupt();
                }

                int row = (int) (Math.random() * BOARD_SIZE);
                int col = (int) (Math.random() * BOARD_SIZE);
                Cell cell = board.getCell(row, col);

                if (cell.isEmpty()) {
                    JOptionPane.showMessageDialog(screen, "Bot escolheu uma celula vazia", "",
                            JOptionPane.INFORMATION_MESSAGE);

                } else if (cell.getPokemon().getPokeState() == PokeState.WILD) {
                    boolean captured = this.capturePokemon(cell.getPokemon());
                    if (captured) {
                        game.getWildPokemon().remove(cell.getPokemon());
                        cell.getPokemon().setPokeState(PokeState.NORMAL);

                    } else {
                        JOptionPane.showMessageDialog(game.getScreen(), "Bot tentou capturar o pokemon, mas falhou!",
                                "",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (cell.getPokemon().getPokeState() == PokeState.NORMAL) { // Pokemon is own by player, fight
                                                                                   // with him
                    JOptionPane.showMessageDialog(screen, "Bot encontrou um Pokémon seu, iniciando batalha!", "",
                            JOptionPane.INFORMATION_MESSAGE);
                    game.botFightsPlayer();

                }

                // TODO: enhance bot logic to choose cells more strategically,
                // random makes chance of actual finding pokemon low

                botTurnMsg.dispose();

                game.endBotTurn();
            } else {
                try {
                    Thread.sleep(500); // Sleep briefly to avoid busy-waiting
                } catch (InterruptedException e) {
                    running = false;
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

}