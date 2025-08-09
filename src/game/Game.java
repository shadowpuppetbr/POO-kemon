package game;

import view.Screen;

public class Game {
    private final Screen screen;

    public Game() {
        // Initialize the game screen which contains the board
        screen = new Screen();
    }

    public void start(){
        screen.initializeScreen();
    }

    public Screen getScreen() {
        return screen;
    }
}
