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
import java.util.ArrayList;

/**
 * User: Franjo
 * Date: 25.10.13
 * Time: 23:48
 * Project: Main
 */
public class Player extends GameObject {
    public static final String TAG = Player.class.getSimpleName();

    private final float epsilon = Constants.EPSILON;

    private static Player instance;

    private IGameInputController input;
    private PlayScreen playScreen;
    private Camera camera;
    private Map map;
    private World world;

    private float vX;
    private float vY;
    private float vX_MAX = 3.2f * 60;
    private float vY_MAX = 3.2f * 60;
    private float aX;
    private float aY;
    private float z;

    private Texture outline;

    public Player(PlayScreen playScreen) {
        super();

        this.playScreen = playScreen;
        map = playScreen.playController.world.map;
        camera = playScreen.playController.camera;

//        init();
    }

//    public static Player getInstance(){
//        if(instance == null){
//            instance = new Player()
//        }
//    }

    public Player(Camera camera, World world) {
        super();

        this.camera = camera;
        this.world = world;

//        map = world.
////        map = world.map;
//
////        init();
    }

    public void init() {

        map = Map.getInstance();

        position.x = map.getSpawn().x;
        position.y = map.getSpawn().y;

        dimension.x = 32;
        dimension.y = 32;

        setPosition(position.x, position.y);
        setSize(dimension.x, dimension.y);

//        setBounds(position.x, position.y, dimension.x, dimension.y);

        System.out.println("bounds: " + getBoundingRectangle().toString());

        setOrigin(0, 0);

        // draw rectangular shape
        Pixmap p = new Pixmap((int) (dimension.x), (int) (dimension.y), Pixmap.Format.RGBA8888);
        Texture t = new Texture(p.getWidth(), p.getHeight(), Pixmap.Format.RGBA8888);

        p.setColor(0, 0, 0, 1);
        p.fillRectangle(0, 0, (int) getWidth(), (int) getHeight());
        p.setColor(1, 1, 1, 1);
        p.fillRectangle((int) origin.x, (int) origin.y, 5, 5);
        p.setColor(1, 0, 0, 1);
        p.drawLine((int) getX(), (int) getY(), (int) (getWidth() + 20), (int) getY());
        p.drawLine((int) getX(), (int) getY(), (int) (getX()), (int) getHeight() + 20);

        t.draw(p, 0, 0);

        setTexture(t);


//        Pixmap pOutline = new Pixmap((int) (dimension.x), (int) (dimension.y), Pixmap.Format.RGBA8888);
//        pOutline.setColor(1, 0, 0, 1);
////        t.getTextureData().prepare();
//        pOutline.drawPixmap(t.getTextureData().consumePixmap(), 0, 0);
//        Texture o = new Texture(32,32, Pixmap.Format.RGBA8888);
//        o.draw(pOutline,0,0);
////        t.draw(pOutline, 0, 0);
//        setTexture(o);
//        setSize(32, 32);
//        position.x = Constants.VIEWPORT_WIDTH / 2 + 50;
//        position.y = Constants.VIEWPORT_HEIGHT / 2;

//        position.x = 0;//Constants.VIEWPORT_WIDTH / 2 + 50;
//        position.y = 0;//Constants.VIEWPORT_HEIGHT / 2;
//
//        dimension.x = getTexture().getWidth();
//        dimension.y = getTexture().getHeight();
//        setOrigin(0, 0);

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

        System.out.println("DIMENSION:" + dimension.x + " " + dimension.y);

    }

    @Override
    public void render(SpriteBatch batch) {

        batch.begin();
        batch.draw(getTexture(), getX(), getY());
//        draw(batch);
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

        vX = input.get_stickX() * deltaTime * vX_MAX;
        vY = input.get_stickY() * deltaTime * vY_MAX;


        setCollisionPosition();

//        position.x += vX * deltaTime * input.get_stickX();
//        position.y += vY * deltaTime * input.get_stickY();


        setPosition(position.x, position.y);
    }

    /**
     * detects whether the player collides with a solid
     * and sets his position depending on the solids
     * position and dimension
     */
    private void setCollisionPosition() {

        /* --- COLLISION DETECTION --- */

        // helper variables
        float qX = position.x + vX;
        float qY = position.y + vY;


        /* MAP COLLISION */
        if (qX < map.position.x + map.borderWidth)
            qX = map.position.x + map.borderWidth;
        else if (qX + dimension.x > map.position.x + map.dimension.x - map.borderWidth)
            qX = map.position.x + map.dimension.x - map.borderWidth - dimension.x;
        if (qY > map.dimension.y - map.borderWidth - 64)// map.getOffsetX() * map.getTileHeight())
            qY = map.dimension.y - map.borderWidth - 64;//map.getOffsetX() * map.getTileHeight();
        else if (qY < map.getY() + map.borderWidth + 64)
            qY = map.getY() + map.borderWidth + 64;



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

        /* object collision test */
//        ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
//        Rectangle[] rects = new Rectangle[10];
        if (map.getCollisionObjects(position.x, position.y).size != 0) {
            System.out.println("bl");
        }

        if (map.getCollisionObjects(position.x + dimension.x, position.y).size != 0) {
            System.out.println("br");
        }

        if (map.getCollisionObjects(position.x, position.y + dimension.y).size != 0) {
            System.out.println("tl");
        }

        if (map.getCollisionObjects(position.x + dimension.x, position.y + dimension.y).size != 0) {
            System.out.println("tr");
        }
//        Array<Rectangle> rectList = map.getCollisionObjects(position.x, position.y + dimension.y); // bl
//        rectList.addAll(map.getCollisionObjects(position.x + dimension.x, position.y)); // br
//        rectList.addAll(map.getCollisionObjects(position.x, position.y + dimension.y)); // tl
//        rectList.addAll(map.getCollisionObjects(position.x + dimension.x, position.y + dimension.y)); // tr
//
////        for (int i = 0; i < map.getCollisionObjects(position.x, position.y).size; i++) {
////            rectList.add(map.getCollisionObjects(position.x, position.y).get(i));
////        }
////        rectList.addAll(map.getCollisionObjects(position.x,position.y));
//        if (rectList.size != 0) {
//            System.out.println("collides");
//        }


//        /* COLLIDED OBJECTS */
//        Array<Rectangle> rectList = null; // = map.getCollisionObjects(position.x, position.y);
//
//        if (vX < 0) {
//            if (qX == position.x) {
//                float _x = Float.MIN_VALUE;
//                rectList = map.getCollisionObjects(qX, position.y + dimension.y);
////                rectList.addAll(map.getCollisionObjects(qX, position.y + dimension.y));
////                rectList.addAll(map.getCollisionObjects(qX, position.y + dimension.y));
//                if (rectList.size != 0) System.out.println("collides");
//
//                for (int i = 0; i < rectList.size; i++) {
//                    Rectangle r = rectList.get(i);
//                    System.out.println(r.getX() + " " + r.getY() + " " + r.getWidth() + " " + r.getHeight());
////                    System.out.println(rectLis);
//                    if (rectList.get(i).getX() + rectList.get(i).getWidth() > _x) {
//                        _x = rectList.get(i).getX() + rectList.get(i).getWidth();
//                    } else {
//                        position.x = _x;
//
//                    }
////                    rectList = map.getCollisionObjects(position.x, position.y);
////                    rectList.addAll(map.getCollisionObjects(position.x, position.y - dimension.y));
//                }
//
//            }
//        }

//        Array<Rectangle> r = null;
//        // bottom left
//        if (vX < 0) {
//            r = map.getCollisionObjects(position.x, position.y);
//
//        }


//        if (r.size != 0) {
//            for (int i = 0; i < r.size; i++) {
//                if (vX < 0) {
//
//                }
//            }
//            if (vX < 0) {
//                position.x = r.
//            }
//            System.out.println("collided!");
//        }

        //push


    }

    public float getvX() {
        return vX;
    }

    public float getvY() {
        return vY;
    }

    public float getvX_MAX() {
        return vX_MAX;
    }

    public float getvY_MAX() {
        return vY_MAX;
    }

    private void log() {
        if (input.get_isA()) Gdx.app.log("BUTTON PRESSED", "[A]");
        if (input.get_isB()) Gdx.app.log("BUTTON PRESSED", "[B]");
    }
}
