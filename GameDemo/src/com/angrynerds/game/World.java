package com.angrynerds.game;

import com.angrynerds.game.camera.CameraHelper;
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
    private Background background;

    private OrthographicCamera camera;
    private CameraHelper cameraHelper;

    /**
     * creates a new World
     *
     * @param camera Camera that is used for gameplay
     */
    public World(OrthographicCamera camera) {
        Gdx.app.log(TAG, " created");

        this.camera = camera;

        init();
    }

    /**
     * initializes the World
     */
    private void init() {
        // world objects
        player = new Player(camera, this);
        map = new Map(this, player);
        background = new Background(this);

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
        map.update(deltaTime);
        background.update(deltaTime);
    }

    /**
     * renders all world objects
     *
     * @param batch SpriteBatch that is used for rendering
     */
    public void render(SpriteBatch batch) {
        cameraHelper.update(Gdx.graphics.getDeltaTime());

        map.render(batch);
        background.render(batch);

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
    public Background getBackground() {
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
