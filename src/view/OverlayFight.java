package view;

import game.Pokemon;
import java.awt.*;
import javax.swing.*;

public class OverlayFight extends JPanel {
    // Dano temporário
    private Integer damageValue = null;
    private boolean damageOnPlayer = false;
    private Timer damageTimer;
    // Mensagem temporária de turno
    private String turnMessage = null;
    private Timer turnMessageTimer;
    // Variáveis de animação
    private int playerOffsetX = 0;
    private int botOffsetX = 0;
    private boolean playerFlash = false;
    private boolean botFlash = false;
    private Timer playerAttackTimer;
    private Timer botAttackTimer;
    private Timer playerFlashTimer;
    private Timer botFlashTimer;
    private ImageIcon spritePlayer;
    private ImageIcon spriteBot;
    private JProgressBar hpPlayer;
    private JProgressBar hpBot;
    private JButton btnAttack;
    private JButton btnRun;
    private JFrame parent;

    public OverlayFight(JFrame parent, Pokemon playerPokemon, Pokemon botPokemon) {
    damageTimer = null;
        turnMessageTimer = null;
        this.parent = parent;
        this.spritePlayer = new ImageIcon(playerPokemon.getImage().getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        this.spriteBot = new ImageIcon(botPokemon.getImage().getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));

        // Inicializa timers como null
        playerAttackTimer = null;
        botAttackTimer = null;
        playerFlashTimer = null;
        botFlashTimer = null;

        setOpaque(false); // Transparente

        // Layout absoluto para posicionamento manual
        setLayout(null);

    // HP Player
    hpPlayer = new JProgressBar(0, playerPokemon.getHp());
    hpPlayer.setValue(playerPokemon.getHp());
    hpPlayer.setStringPainted(true);
    hpPlayer.setString(playerPokemon.getHp() + " HP");

    // HP Bot
    hpBot = new JProgressBar(0, botPokemon.getHp());
    hpBot.setValue(botPokemon.getHp());
    hpBot.setStringPainted(true);
    hpBot.setString(botPokemon.getHp() + " HP");

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
                adjustLayout();
            }
        });

        // Define este painel como GlassPane
        parent.setGlassPane(this);
        setVisible(true);
    }

    private void adjustLayout() {
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

        // Sprites com animação de movimento
        int playerX = 50 + playerOffsetX;
        int botX = w - spriteBot.getIconWidth() - 50 - botOffsetX;
        int playerY = h / 2 - spritePlayer.getIconHeight() / 2;
        int botY = h / 2 - spriteBot.getIconHeight() / 2;

        // Piscar branco se necessário
        if (spritePlayer != null) {
            if (playerFlash) {
                g2.setColor(Color.WHITE);
                g2.fillRect(playerX, playerY, spritePlayer.getIconWidth(), spritePlayer.getIconHeight());
            }
            spritePlayer.paintIcon(this, g2, playerX, playerY);
        }
        if (spriteBot != null) {
            if (botFlash) {
                g2.setColor(Color.WHITE);
                g2.fillRect(botX, botY, spriteBot.getIconWidth(), spriteBot.getIconHeight());
            }
            spriteBot.paintIcon(this, g2, botX, botY);
        }
        adjustLayout();

        g2.dispose();

        // Desenhar mensagem de turno, se houver
        if (turnMessage != null) {
            Graphics2D gMsg = (Graphics2D) g.create();
            gMsg.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = gMsg.getFontMetrics();
            int msgWidth = fm.stringWidth(turnMessage);
            int x = (getWidth() - msgWidth) / 2;
            int y = 80;
            gMsg.setColor(new Color(255, 255, 0, 220));
            gMsg.drawString(turnMessage, x, y);
            gMsg.dispose();
        }

        // Desenhar valor de dano, se houver
        if (damageValue != null) {
            Graphics2D gDmg = (Graphics2D) g.create();
            gDmg.setFont(new Font("Arial", Font.BOLD, 58));
            gDmg.setColor(new Color(255, 50, 50, 230));
            String dmgStr = "-" + damageValue;
            int x, y;
            w = getWidth();
            h = getHeight();
            if (!damageOnPlayer) {
                // Próximo ao player
                x = 50 + playerOffsetX + 10;
                y = h / 2 - spritePlayer.getIconHeight() / 2 - 10;
            } else {
                // Próximo ao bot
                x = w - spriteBot.getIconWidth() - 50 + 10;
                y = h / 2 - spriteBot.getIconHeight() / 2 - 10;
            }
            gDmg.drawString(dmgStr, x, y);
            gDmg.dispose();
        }
    }
    /**
     * Exibe o valor do dano próximo ao Pokémon atingido
     * @param value valor do dano
     * @param isPlayer true para mostrar no player, false para mostrar no bot
     */
    public void showDamage(int value, boolean isPlayer) {
        this.damageValue = value;
        this.damageOnPlayer = isPlayer;
        repaint();
        if (damageTimer != null && damageTimer.isRunning()) damageTimer.stop();
        damageTimer = new Timer(900, e -> {
            damageValue = null;
            repaint();
        });
        damageTimer.setRepeats(false);
        damageTimer.start();
    }
    

    /**
     * Exibe uma mensagem de turno temporária na tela
     * @param msg texto a ser exibido
     * @param millis duração em milissegundos
     */
    public void showTurnMessage(String msg, int millis) {
        this.turnMessage = msg;
        repaint();
        if (turnMessageTimer != null && turnMessageTimer.isRunning()) turnMessageTimer.stop();
        turnMessageTimer = new Timer(millis, e -> {
            turnMessage = null;
            repaint();
        });
        turnMessageTimer.setRepeats(false);
        turnMessageTimer.start();
    }

    // Método para atualizar HP
    public void setHpPlayer(int value) {
    hpPlayer.setValue(value);
    hpPlayer.setString(value + " HP");
    adjustLayout();
    }

    public void setHpBot(int value) {
    hpBot.setValue(value);
    hpBot.setString(value + " HP");
    adjustLayout();
    }

    public JButton getBtnAttack() {
        return btnAttack;
    }

    public JButton getBtnRun() {
        return btnRun;
    }

    // Animação de ataque do player
    public void animateAttackPlayer() {
        if (playerAttackTimer != null && playerAttackTimer.isRunning()) playerAttackTimer.stop();
        playerOffsetX = 0;
        playerAttackTimer = new Timer(15, null);
        playerAttackTimer.addActionListener(e -> {
            playerOffsetX += 10;
            if (playerOffsetX >= 60) {
                playerAttackTimer.stop();
                // Volta para posição original
                Timer backTimer = new Timer(15, null);
                backTimer.addActionListener(ev -> {
                    playerOffsetX -= 10;
                    if (playerOffsetX <= 0) {
                        playerOffsetX = 0;
                        backTimer.stop();
                    }
                    repaint();
                });
                backTimer.start();
            }
            repaint();
        });
        playerAttackTimer.start();
    }

    // Animação de ataque do bot
    public void animateAttackBot() {
        if (botAttackTimer != null && botAttackTimer.isRunning()) botAttackTimer.stop();
        botOffsetX = 0;
        botAttackTimer = new Timer(15, null);
        botAttackTimer.addActionListener(e -> {
            botOffsetX += 10;
            if (botOffsetX >= 60) {
                botAttackTimer.stop();
                // Volta para posição original
                Timer backTimer = new Timer(15, null);
                backTimer.addActionListener(ev -> {
                    botOffsetX -= 10;
                    if (botOffsetX <= 0) {
                        botOffsetX = 0;
                        backTimer.stop();
                    }
                    repaint();
                });
                backTimer.start();
            }
            repaint();
        });
        botAttackTimer.start();
    }

    // Piscar branco no player
    public void flashPlayer() {
        if (playerFlashTimer != null && playerFlashTimer.isRunning()) playerFlashTimer.stop();
        playerFlash = true;
        repaint();
        playerFlashTimer = new Timer(60, null);
        playerFlashTimer.setRepeats(false);
        playerFlashTimer.addActionListener(e -> {
            playerFlash = false;
            repaint();
        });
        playerFlashTimer.start();
    }

    // Piscar branco no bot
    public void flashBot() {
        if (botFlashTimer != null && botFlashTimer.isRunning()) botFlashTimer.stop();
        botFlash = true;
        repaint();
        botFlashTimer = new Timer(60, null);
        botFlashTimer.setRepeats(false);
        botFlashTimer.addActionListener(e -> {
            botFlash = false;
            repaint();
        });
        botFlashTimer.start();
    }
}
