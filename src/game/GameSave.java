package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GameSave {

    public static void saveGame(Game game) {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Salvar Jogo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("POOkemon Save File (*.sav)", "sav"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getPath();
            if (!filePath.toLowerCase().endsWith(".sav")) {
                fileToSave = new File(filePath + ".sav");
            }

            try (FileOutputStream fileOut = new FileOutputStream(fileToSave);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(game);
                JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!");
            } catch (IOException i) {
                i.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao salvar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static Game loadGame() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Carregar Jogo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("POOkemon Save File (*.sav)", "sav"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (FileInputStream fileIn = new FileInputStream(fileToLoad);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                Game game = (Game) in.readObject();
                JOptionPane.showMessageDialog(null, "Jogo carregado com sucesso!");
                return game;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao carregar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
}