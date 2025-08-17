package view;

import game.Board;
import game.Pokemon;
import java.awt.*;
import javax.swing.*;

public class Screen extends JFrame {
    private final Board board;

    private JButton debugButton;
    private JButton hintButton;
    private JButton changePokemonButton;
    private JButton endTurnButton;
    private JButton saveButton;
    private JButton exitButton;
    private JLabel scoreLabel;
    private JPanel infoPanel;
    private JLabel mainPokemonImg;
    private JPanel mainPokemonPanel;
    private JLabel mainPokemonTxt;



    public Screen(Board board) {
        super();
        this.board = board;
    }

    public void initializeScreen() {
        setTitle("POOkemon");
        setSize(960,720);

        Image icon = new ImageIcon("src/resources/images/pokeball.png").getImage();
        setIconImage(icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setResizable(false);
    
        
        // LEFT board
        this.board.setPreferredSize(new Dimension(640, 640));
        JPanel boardWrapper = new JPanel();
        boardWrapper.setLayout(new FlowLayout());
        boardWrapper.add(this.board);
        add(boardWrapper);

        // RIGHT game info
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(260, 640));
        rightPanel.setLayout(new GridLayout(2, 1, 10, 0));
        add(rightPanel);

        // Info
        infoPanel = new JPanel(new GridLayout(2, 1));
        scoreLabel = new JLabel("Placar: 0");
        scoreLabel.setSize(100, 50);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 26));
        infoPanel.add(scoreLabel);

        mainPokemonPanel = new JPanel();
        mainPokemonPanel.setLayout(new GridLayout(1,2));

        mainPokemonImg = new JLabel(new ImageIcon());
        mainPokemonImg.setHorizontalAlignment(SwingConstants.LEFT);
        mainPokemonPanel.add(mainPokemonImg);

        mainPokemonTxt = new JLabel();
        mainPokemonTxt.setFont(new Font("Arial", Font.BOLD, 18));
        mainPokemonTxt.setHorizontalAlignment(SwingConstants.CENTER);
        mainPokemonPanel.add(mainPokemonTxt);

        infoPanel.add(mainPokemonPanel);


        rightPanel.add(infoPanel);

        // Control Buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6, 1, 10, 10));
        debugButton = new JButton("Debug");
        hintButton = new JButton("Dica");
        changePokemonButton = new JButton("Ver Time");
        endTurnButton = new JButton("Finalizar Turno");
        saveButton = new JButton("Salvar Jogo");
        exitButton = new JButton("Sair do Jogo");
        controlPanel.add(debugButton);
        controlPanel.add(hintButton);
        controlPanel.add(changePokemonButton);
        controlPanel.add(endTurnButton);
        controlPanel.add(saveButton);
        controlPanel.add(exitButton);
        
        rightPanel.add(controlPanel);


        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public Board getBoard() {
        return this.board;
    }

    public JButton getDebugButton() {
        return debugButton;
    }

    public JButton getHintButton(){
        return hintButton;
    }

    public JButton getChangePokemonButton() {
        return changePokemonButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    public JButton getSaveButton() { // Getter para o novo botão
        return saveButton;
    }

    public void updateScore(int score){
        String msg = "Placar: " + score;
        scoreLabel.setText(msg);
    }

    public void updateMainPokemon(Pokemon pokemon) {
        ImageIcon icon = pokemon.getImage();
        icon = new ImageIcon(icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
        mainPokemonImg.setIcon(icon);
        mainPokemonTxt.setText("<html>" + pokemon.getName() + "<br/>Vida: " + pokemon.getHp() + "<br/>Nível: " + pokemon.getLevel() + "</html>");
        repaint();
    }
}