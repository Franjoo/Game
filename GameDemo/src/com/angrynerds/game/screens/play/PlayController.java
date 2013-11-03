package com.angrynerds.game.screens.play;

import com.angrynerds.game.World;
import com.angrynerds.util.Constants;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * represents a class which is responsible for the camera of the gameplay and for
 * updating the world
 */
public class PlayController {
    private static final String TAG = PlayController.class.getSimpleName();

    private World world;
    private OrthographicCamera camera;

    /**
     * creates an new PlayController
     */
    public PlayController() {
        init();
    }

    /**
     * initializes PlayController
     */
    private void init() {
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.update();

        world = new World(camera);
    }

    /**
     * updates the objects used in PlayScreen
     *
     * @param deltaTime time since last frame
     */
    public void update(float deltaTime) {
        world.update(deltaTime);
    }

    /**
     * returns the world in which the game is taking place
     */
    public World getWorld() {
        return world;
    }

    /**
     * returns the camera which is used in the game play
     */
    public OrthographicCamera getCamera() {
        return camera;
    }
}
