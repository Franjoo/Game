package com.angrynerds.input;

import com.angrynerds.game.screens.PlayScreen;
import com.angrynerds.ui.ControllUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 12:27
 * Project: Main
 */
public class TouchInput implements IGameInputController {

    public ControllUI ui;

    private PlayScreen playScreen;
    private Camera camera;


//    public TouchInput(float centerX, float centerY) {
//        this(new Vector2(centerX, centerY));
//    }

    public TouchInput(PlayScreen playScreen) {
        this.playScreen = playScreen;

        init();
    }
    public TouchInput(Camera camera) {
//        this.playScreen = playScreen;

        this.camera = camera;
        init();
    }

    private void init() {
        ui = new ControllUI(camera);
        Gdx.app.log("!!!!!!!!!! UI","" + ui);
    }

    @Override
    public float get_stickX() {
        if (Gdx.input.isTouched()) {
            return ui.get_stickX();
        }
        return 0;
    }

    @Override
    public float get_stickY() {
        if (Gdx.input.isTouched()) {
            return ui.get_stickY();
        }
        return 0;
    }

    @Override
    public boolean get_isA() {
        return ui.get_isA();
    }

    @Override
    public boolean get_isB() {
        return ui.get_isB();
    }
}
