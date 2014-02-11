package com.angrynerds.game.screens.play;

import com.angrynerds.game.screens.AbstractScreen;
import com.angrynerds.ui.ControllerUI;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * class that represents the screen which is used for game play
 */
public class PlayScreen extends AbstractScreen {
    private static final String TAG = PlayScreen.class.getSimpleName();

    private PlayController playController;
    private PlayRenderer playRenderer;


    private static SpriteBatch batch;

    /**
     * creates a new PlayScreen.
     * PlayScreen provides static access to the SpriteBatch which
     * is used for rendering
     */
    public PlayScreen() {
        super();

        // allow static access, note: ugly code practice
        batch = super.getSpriteBatch();

    }

    /**
     * initializes PlayScreen
     */
    private void init() {
        playController = new PlayController();
        playRenderer = new PlayRenderer(playController, batch);

    }

    /**
     * returns the PlayController which is used in the PlayScreen
     */
    public PlayController getPlayController() {
        return playController;
    }

    /**
     * returns the PlayRenderer which is used in the PlayScreen
     */
    public PlayRenderer getPlayRenderer() {
        return playRenderer;
    }

    /**
     * returns the static SpriteBatch which is used for rendering the PlayScreen
     */
    public static SpriteBatch getBatch() {
        return batch;
    }


    @Override
    public void update(float deltaTime) {
        playController.update(deltaTime);
    }

    @Override
    public void render(float deltaTime) {
        playRenderer.render(deltaTime);
    }

    @Override
    public void resize(int width, int height) {
        playRenderer.resize(width, height);
    }

    @Override
    public void show() {
        init();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}
