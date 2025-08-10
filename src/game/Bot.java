package game;

import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Bot extends Trainer implements Runnable{
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }
    
    @Override
    protected void chooseCell() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void run() {
        
        JDialog botTurnMsg = new JDialog(game.getScreen(), "Turno do Bot", false);
        botTurnMsg.setSize(300, 100);
        botTurnMsg.setLocationRelativeTo(game.getScreen());
        botTurnMsg.setLayout(new FlowLayout());
        botTurnMsg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        botTurnMsg.add(new JLabel("O Bot está pensando..."));
        botTurnMsg.setVisible(true);

        try {
            Thread.sleep(1500); // Espera 1.5 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // TODO Bot's action

        botTurnMsg.dispose();
        JOptionPane.showMessageDialog(game.getScreen(), "O Bot atacou!");

        game.endBotTurn(); // Sinaliza para o jogo que o turno do Bot terminou

    }
}
