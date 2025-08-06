package game;

import view.Screen;

public class Game {
    private Screen screen;

    public Game() {
        // Initialize the game screen which contains the board
        screen = new Screen();
    }

    public Screen getScreen() {
        return screen;
    }
}
