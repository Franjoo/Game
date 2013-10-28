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


    //    // Screens
//    private PlayScreen playScreen;
//    private MainMenuScreen mainMenuScreen;
    public static SpriteBatch batch;
    private GameController gameController;

    public GameRenderer(GameController gameController) {
        this.gameController = gameController;

        init();
    }

    private void init() {
        batch = new SpriteBatch();
//        playScreen = new PlayScreen();
//        playScreen.toString();
    }

    public void resize(int width, int heigth) {
    }


    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

//        batch.begin();
        gameController.playScreen.render(deltaTime, batch);
//        batch.end();

//        playScreen.render(deltaTime);
    }

    @Override
    public void dispose() {
    }
}
