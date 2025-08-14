package view;

import javax.swing.*;

import game.Pokemon;

import java.awt.*;

public class OverlayFight extends JPanel {
    private ImageIcon spritePlayer;
    private ImageIcon spriteBot;
    private JProgressBar hpPlayer;
    private JProgressBar hpBot;
    private JButton btnAttack;
    private JButton btnRun;
    private JFrame parent;

    public OverlayFight(JFrame parent, Pokemon playerPokemon, Pokemon botPokemon) {
        this.parent = parent;
        this.spritePlayer = playerPokemon.getImage();
        this.spriteBot = botPokemon.getImage();

        setOpaque(false); // Transparente

        // Layout absoluto para posicionamento manual
        setLayout(null);

        // HP Player
        hpPlayer = new JProgressBar(0, 100);
        hpPlayer.setValue(playerPokemon.getHp());
        hpPlayer.setStringPainted(true);

        // HP Bot
        hpBot = new JProgressBar(0, 100);
        hpBot.setValue(botPokemon.getHp());
        hpBot.setStringPainted(true);

        // Botões
        btnAttack = new JButton("Atacar");
        btnRun = new JButton("Fugir");

        add(hpPlayer);
        add(hpBot);
        add(btnAttack);
        add(btnRun);

        // Ajusta posições após saber tamanho do parent
        parent.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                ajustarLayout();
            }
        });

        // Define este painel como GlassPane
        parent.setGlassPane(this);
        setVisible(true);
    }

    private void ajustarLayout() {
        JFrame parent = this.parent;
        int w = parent.getWidth();
        int h = parent.getHeight();

        // HP bars
        hpPlayer.setBounds(50, h / 2 - 120, 120, 20);
        hpBot.setBounds(w - 170, h / 2 - 120, 120, 20);

        // Botões
        btnAttack.setBounds(w / 2 - 110, h - 80, 100, 40);
        btnRun.setBounds(w / 2 + 10, h - 80, 100, 40);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Faixa central semi-transparente
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, h / 2 - 100, w, 200);

        // Sprites
        if (spritePlayer != null) {
            spritePlayer.paintIcon(this, g2, 50, h / 2 - spritePlayer.getIconHeight() / 2);
        }
        if (spriteBot != null) {
            spriteBot.paintIcon(this, g2, w - spriteBot.getIconWidth() - 50, h / 2 - spriteBot.getIconHeight() / 2);
        }
        ajustarLayout();

        g2.dispose();
    }

    // Método para atualizar HP
    public void setHpPlayer(int value) {
        hpPlayer.setValue(value);
        ajustarLayout();
    }

    public void setHpBot(int value) {
        hpBot.setValue(value);
        ajustarLayout();
    }

    public JButton getBtnAttack() {
        return btnAttack;
    }

    public JButton getBtnRun() {
        return btnRun;
    }
}
