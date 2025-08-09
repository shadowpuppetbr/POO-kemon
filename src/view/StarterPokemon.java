package view;

import game.Pokemon;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class StarterPokemon extends JDialog{

    private Pokemon selectedPokemon;

    public StarterPokemon(Frame owner, Pokemon[] pokemon) {
        super(owner, "Escolha seu Pokémon Inicial", true);  // modal = true to pause program execution
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(640, 400);
        setLocationRelativeTo(owner);


        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        mainPanel.setLayout(new GridLayout(4, 4, 10, 10));
        
        for (Pokemon poke : pokemon) {
            JButton pokeButton = new JButton(poke.getName());
            pokeButton.setIcon(poke.getImage());

            pokeButton.addActionListener(_ -> {
                selectedPokemon = poke;
                dispose();
            });
            mainPanel.add(pokeButton);
        }

    }

    public Pokemon getSelectedPokemon(){
        return selectedPokemon;
    }



}