package com.angrynerds.game;

import com.angrynerds.game.screens.MainMenuScreen;
import com.angrynerds.game.screens.PlayScreen;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 22:09
 * Project: GameDemo
 */
public class GameRenderer implements Disposable {

    // gamestates
    private static String STATE;
    private static final String PLAY = "play";
    private static final String MENU = "menu";

    public static SpriteBatch batch;
    private GameController gameController;


    public GameRenderer(GameController gameController) {
        this.gameController = gameController;

        init();
    }

    private void init() {
        batch = new SpriteBatch();

        STATE = PLAY;
    }

    public void resize(int width, int heigth) {
    }


    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (STATE == PLAY) {
            gameController.playScreen.render(deltaTime, batch);
        }
    }

    @Override
    public void dispose() {
    }
}
