package com.angrynerds.game.screens;

import com.angrynerds.camera.CameraHelper.CameraHelper;
import com.angrynerds.game.GameController;
import com.angrynerds.game.PlayController;
import com.angrynerds.game.PlayRenderer;
import com.angrynerds.ui.ControllUI;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 22:53
 * Project: GameDemo
 */
public class PlayScreen extends AbstractScreen {
    private static final String TAG = PlayScreen.class.getSimpleName();

    public GameController gameController;

    public PlayController playController;
    public PlayRenderer playRenderer;

    // controll ui
    private static ControllUI controllUI;

//    // camera
//    private OrthographicCamera camera;
//    private CameraHelper cameraHelper;

//    // gameobjects
//    private Player player;
//    private Map map;

    public PlayScreen(GameController gameController) {
        super();

        this.gameController = gameController;
        init();
    }

    private void init() {

//        if(Gdx.app.getType() == Application.ApplicationType.Android){
//            controllUI = new ControllUI(playController.camera, new SpriteBatch());
//        }

        playController = new PlayController(this);
        playRenderer = new PlayRenderer(playController);

//        // camera
//        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
////        camera.position.set(0, 0, 0);
////        camera.setToOrtho(true);
//        camera.update();
//        cameraHelper = new CameraHelper();
//        cameraHelper.applyTo(camera);
//
//        // player
//        player = new Player();
//        cameraHelper.setTarget(player);
//
//        // map
//        map = new Map(camera);
    }


    @Override
    public void update(float deltaTime) {
        playController.update(deltaTime);
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        batch.setProjectionMatrix(playController.camera.combined);

        playRenderer.render(deltaTime,batch);

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void render(float deltaTime) {

//
//        // update
//        player.update(deltaTime);
//        map.update(deltaTime);
//        cameraHelper.update(deltaTime);
//        cameraHelper.applyTo(camera);
//        batch.setProjectionMatrix(camera.combined);
//
//
//        map.render(batch);
////        player.render(batch);
//
//        batch.begin();
//
//        player.draw(batch);
//        batch.end();
//

    }

    @Override
    public void resize(int i, int i2) {
    }

    @Override
    public void show() {
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

    @Override
    public void dispose() {

    }

//    public static ControllUI getControllUI() {
//        return controllUI;
//    }
}
