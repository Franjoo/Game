package com.angrynerds.game;

import com.angrynerds.camera.CameraHelper.CameraHelper;
import com.angrynerds.game.screens.PlayScreen;
import com.angrynerds.gameobjects.Player;
import com.angrynerds.ui.ControllUI;
import com.angrynerds.util.Constants;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * User: Franjo
 * Date: 27.10.13
 * Time: 12:47
 * Project: GameDemo
 */
public class PlayController {
    private static final String TAG = PlayController.class.getSimpleName();

    private PlayScreen playScreen;

    // gameobjects
    public Player player;
    public World world;

    // camera
    public OrthographicCamera camera;
    private CameraHelper cameraHelper;

    public PlayController(PlayScreen playScreen) {
        this.playScreen = playScreen;
        init();
    }

    private void init() {
        // camera
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
//        camera.position.set(0, 0, 0);
//        camera.setToOrtho(true);
        camera.update();
        cameraHelper = new CameraHelper(this);
        cameraHelper.applyTo(camera);

        System.out.println( "camera: " + camera);

        world = new World(camera);
        player = new Player(camera,world);

        cameraHelper.setTarget(player);


    }


    public void update(float deltaTime) {
        cameraHelper.update(deltaTime);
        cameraHelper.applyTo(camera);

        player.update(deltaTime);
        world.update(deltaTime);
    }


}
