package com.angrynerds.gameobjects;

import com.angrynerds.game.PlayController;
import com.angrynerds.game.World;
import com.angrynerds.game.screens.PlayScreen;
import com.angrynerds.input.IGameInputController;
import com.angrynerds.input.KeyboardInput;
import com.angrynerds.input.TouchInput;
import com.angrynerds.input.gamepads.X360Gamepad;
import com.angrynerds.util.Constants;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import javax.swing.text.DefaultEditorKit;

/**
 * User: Franjo
 * Date: 25.10.13
 * Time: 23:48
 * Project: Main
 */
public class Player extends GameObject {
    public static final String TAG = Player.class.getSimpleName();

    private final float epsilon = Constants.EPSILON;

    private IGameInputController input;
    private PlayScreen playScreen;
    private Camera camera;
    private Map map;
    private World world;

    private float vX = 5 * 60;
    private float vY = 5 * 60;
    private float aX;
    private float aY;

    public Player(PlayScreen playScreen) {
        super();

        this.playScreen = playScreen;
        map = playScreen.playController.world.map;
        camera = playScreen.playController.camera;

        init();
    }

    public Player(Camera camera, World world) {
        super();

        this.camera = camera;

        this.world = world;
        map = world.map;

        init();
    }

    private void init() {

        // draw rectangular shape
        Pixmap p = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 1);
        p.drawRectangle(0, 0, 32, 32);

        setTexture(new Texture(p));
        setSize(32, 32);
//        position.x = Constants.VIEWPORT_WIDTH / 2 + 50;
//        position.y = Constants.VIEWPORT_HEIGHT / 2;

        position.x = 0;//Constants.VIEWPORT_WIDTH / 2 + 50;
        position.y = 0;//Constants.VIEWPORT_HEIGHT / 2;

        dimension.x = getTexture().getWidth();
        dimension.y = getTexture().getHeight();
        setOrigin(0, 0);

        // input
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Array controllers = Controllers.getControllers();
            if (controllers.size != 0) {
                input = new X360Gamepad((Controller) controllers.get(X360Gamepad.NUM_CONTROLLERS));
            } else {
                input = new KeyboardInput();
//                input = new TouchInput(camera);
            }
        } else if (Gdx.app.getType() == Application.ApplicationType.Android) {
            input = new TouchInput(camera);
        }


        // some basic init assertions
        assert (input != null) : (TAG + ": input must not be null");

    }

    @Override
    public void render(SpriteBatch batch) {

        batch.begin();
        draw(batch);
        batch.end();


//        draw(i.);

//        batch.begin();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            TouchInput i = (TouchInput) input;
            i.ui.render(batch);
//        batch.end();
        }
//        batch.end();
//        batch.draw(getTexture(), position.x, position.y, origin.x, origin.y, dimension.x / 4, dimension.y / 4,
//                scale.x, scale.y);

    }

    @Override
    public void update(float deltaTime) {

//        System.out.println("P: " + position.x + ", " + position.y);

        vX = input.get_stickX() * deltaTime * 5 * 60;
        vY = input.get_stickY() * deltaTime * 5 * 60;

        setCollisionPosition();

//        position.x += vX * deltaTime * input.get_stickX();
//        position.y += vY * deltaTime * input.get_stickY();

        setPosition(position.x, position.y);
    }

    private void setCollisionPosition() {

        /* --- COLLISION DETECTION --- */

        // helper variables
        float qX = position.x + vX;
        float qY = position.y + vY;


        /* COLLIDED TILES */

        /* X-AXIS */
        if (vX < 0) {
            // botton left
            if (map.isSolid(qX, position.y)) {
                position.x = ((int) (position.x) / map.getTileWidth()) * map.getTileWidth();
            }
            // top left
            else if (map.isSolid(qX, position.y + dimension.y)) {
                position.x = ((int) (position.x) / map.getTileWidth()) * map.getTileWidth();
            } else {
                position.x = qX;
            }
        } else if (vX > 0)
            // bottom right
            if (map.isSolid(qX + dimension.x, position.y)) {
                position.x = ((int) (qX) / map.getTileWidth()) * map.getTileWidth() - epsilon;
            }
            // top right
            else if (map.isSolid(qX + dimension.x, position.y + dimension.y)) {
                position.x = ((int) (qX) / map.getTileWidth()) * map.getTileWidth() - epsilon;
            } else {
                position.x = qX;
            }

        /* Y_AXIS */
        if (vY < 0) {
            // bottom left
            if (map.isSolid(position.x, qY)) {
                position.y = ((int) (position.y) / map.getTileHeight()) * map.getTileHeight();
            }
            // bottom right
            else if (map.isSolid(position.x + dimension.x, qY)) {
                position.y = ((int) (position.y) / map.getTileHeight()) * map.getTileHeight();
            } else {
                position.y = qY;
            }
        } else if (vY > 0) {
            // top left
            if (map.isSolid(position.x, qY + dimension.y)) {
                position.y = ((int) (qY) / map.getTileHeight()) * map.getTileHeight() - epsilon;
            }
            // top right
            else if (map.isSolid(position.x + dimension.x, qY + dimension.y)) {
                position.y = ((int) (qY) / map.getTileHeight()) * map.getTileHeight() - epsilon;
            } else {
                position.y = qY;
            }
        }

        /* COLLIDED OBJECTS */
        Array<Rectangle> r = map.getCollisionObjects(position.x,position.y);

        if(r.size != 0){
            System.out.println("collided!");
        }


    }

    private void log() {
        if (input.get_isA()) Gdx.app.log("BUTTON PRESSED", "[A]");
        if (input.get_isB()) Gdx.app.log("BUTTON PRESSED", "[B]");
    }
}
