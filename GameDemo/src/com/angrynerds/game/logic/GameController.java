package com.angrynerds.game.logic;

import com.angrynerds.game.screens.mainmenu.MainMenuScreen;
import com.angrynerds.game.screens.play.PlayScreen;
import com.badlogic.gdx.utils.Disposable;

/**
 * class that is reponsible for updating and rendering all the
 * screen which are in use
 */
public class GameController implements Disposable {

    // screens
    public PlayScreen playScreen;
    private MainMenuScreen mainMenuScreen;

    // game states
    private static String STATE;
    private static final String PLAY = "play";
    private static final String MENU = "menu";

    public GameController() {
        init();
    }

    /**
     * initializes the screens which should be used
     */
    private void init() {
        playScreen = new PlayScreen();
    }

    /**
     * updates the screens which are currently in use
     *
     * @param deltaTime time since last frame
     */
    public void update(float deltaTime) {
        playScreen.update(deltaTime);
        playScreen.render(deltaTime);
    }

    @Override
    public void dispose() {
        playScreen.dispose();
    }

    /**
     * called when the application is resized
     *
     * @param width  new width in pixels
     * @param height new height in pixels
     */
    public void resize(int width, int height) {
        playScreen.resize(width, height);
    }

    /**
     * called when the application is paused
     */
    public void pause() {
        // TODO
    }

    /**
     * called when the application is resumed from a paused state
     */
    public void resume() {
        // TODO

    }
}
