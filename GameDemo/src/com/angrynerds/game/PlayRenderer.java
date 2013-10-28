package com.angrynerds.game;

import com.angrynerds.ui.ControllUI;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * User: Franjo
 * Date: 27.10.13
 * Time: 12:54
 * Project: GameDemo
 */
public class PlayRenderer {

    private PlayController playController;
    public ControllUI controllUI;

    public PlayRenderer(PlayController playController) {
        this.playController = playController;

        init();
    }

    private void init() {

    }

    public void render(float deltaTime, SpriteBatch batch) {
//        playController.camera.setToOrtho(true);
        batch.setProjectionMatrix(playController.camera.combined);

//        batch.begin();
        playController.world.render(batch);

//        batch.begin();

//        playController.player.draw(batch);
//        batch.begin();

        playController.player.render(batch);
//        batch.end();

//        controllUI.render();
    }



}
