package com.angrynerds.game;

import com.angrynerds.game.camera.CameraHelper;
import com.angrynerds.game.screens.play.PlayController;
import com.angrynerds.game.screens.play.PlayScreen;
import com.angrynerds.gameobjects.Map;
import com.angrynerds.gameobjects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * represents the World in which the game is taking place
 */
public class World {
    public static final String TAG = World.class.getSimpleName();

    private Map map;
    private Player player;
    private Layer background;

    private OrthographicCamera camera;
    private PlayController playController;
    private CameraHelper cameraHelper;

    /**
     * creates a new World
     *
     */
    public World(PlayController playController) {
        Gdx.app.log(TAG, " created");

        this.playController = playController;
        camera = playController.getCamera();

        init();
    }

    /**
     * initializes the World
     */
    private void init() {
        // world objects
        player = new Player(playController.getControllerUI());
        map = new Map(this, player);
        background = new Layer(this);

        // camera
        cameraHelper = new CameraHelper(this);
        cameraHelper.applyTo(camera);
        cameraHelper.setTarget(player);
    }

    /**
     * updates the world
     *
     * @param deltaTime time since last frame
     */
    public void update(float deltaTime) {
        background.update(deltaTime);
        map.update(deltaTime);

        playController.getControllerUI().update(deltaTime);
    }

    /**
     * renders all world objects
     *
     * @param batch SpriteBatch that is used for rendering
     */
    public void render(SpriteBatch batch) {
        cameraHelper.update(Gdx.graphics.getDeltaTime());

        background.render(batch);
        map.render(batch);

        playController.getControllerUI().render();

        cameraHelper.applyTo(camera);


    }

    /**
     * returns the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * returns the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * returns the background
     */
    public Layer getBackground() {
        return background;
    }

    /**
     * returns the camera
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * returns the camera helper
     *
     * @return
     */
    public CameraHelper getCameraHelper() {
        return cameraHelper;
    }

    // TODO es bestehen bisher noch abhaengigkeiten zwischen player und map die nicht sein duerften. player wird in world erzeugt aber in map geupdated, das sollte überdacht werden.


}
