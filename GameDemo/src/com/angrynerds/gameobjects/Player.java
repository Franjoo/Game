package com.angrynerds.gameobjects;

import com.angrynerds.gameobjects.creatures.Creature;
import com.angrynerds.input.DeprecatedTouchInput;
import com.angrynerds.input.IGameInputController;
import com.angrynerds.input.KeyboardInput;
import com.angrynerds.input.TouchInput;
import com.angrynerds.input.gamepads.X360Gamepad;
import com.angrynerds.util.Constants;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;

/**
 * class that represents the Player
 */
public class Player extends Creature {
    private static final String TAG = Player.class.getSimpleName();

    // constants
    private final float epsilon = Constants.EPSILON;

    // map
    private Camera camera;
    private Map map;

    // movement
    private float vX;
    private float vY;
    private float vX_MAX = 500;
    private float vY_MAX = 220;

    // helper attributes
    private Vector2 vec2 = new Vector2();
    private Array<Vector2> collPos = new Array<Vector2>();

    private Vector2 _po = new Vector2();
    private Vector2 _pm = new Vector2();
    private Vector2 _pt = new Vector2();

    // creature relevant attributes
    private AnimationState state;
    private AnimationStateData stateData;
    private Array<Event> events;
    private Animation walkAnimation;
    private Animation jumpAnimation;

    private Animation currentAnimation;

    // input
    private IGameInputController input;


    /**
     * creates a new player
     */
    public Player(IGameInputController input) {
        super("Max_move", "data/spine/max/", null, 0.3f);

        this.input = input;

        walkAnimation = skeletonData.findAnimation("run_test");
        jumpAnimation = skeletonData.findAnimation("jump");

    }

    public void init() {

        // set map
        map = Map.getInstance();

        // set position
//        x = map.getSpawn().x;
//        y = map.getSpawn().y;
        x = 500;
        y = 300;

        setAnimationStates();

    }

    private void setAnimationStates() {
        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
        stateData.setMix("run_test", "jump", 0.2f);
        stateData.setMix("jump", "run_test", 0.4f);
        stateData.setMix("jump", "jump", 0.2f);

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.addListener(new AnimationState.AnimationStateListener() {
            public void event(int trackIndex, Event event) {
                System.out.println(trackIndex + " event: " + state.getCurrent(trackIndex) + ", " + event.getData().getName());
            }

            public void complete(int trackIndex, int loopCount) {
                System.out.println(trackIndex + " complete: " + state.getCurrent(trackIndex) + ", " + loopCount);
            }

            public void start(int trackIndex) {
                System.out.println(trackIndex + " start: " + state.getCurrent(trackIndex));
            }

            public void end(int trackIndex) {
                System.out.println(trackIndex + " end: " + state.getCurrent(trackIndex));
            }
        });
        state.setAnimation(0, "run_test", true);
    }

    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        state.update(deltaTime);
        state.apply(skeleton);


        // set v in x and y direction
        vX = input.get_stickX() * deltaTime * vX_MAX;
        vY = input.get_stickY() * deltaTime * vY_MAX;

        // set collision position
        Vector2 p = getCollisionPosition();

        // update position attributes
        x = p.x;
        y = p.y;

        // apply animation
        skeleton.setFlipX(vX < 0);
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
////            currentAnimation = jumpAnimation;
//            state.setAnimation(0, "jump", false);
//            state.addAnimation(0, "run_test", true, 0);
//        }

         currentAnimation = walkAnimation;
        currentAnimation.apply(skeleton, skeleton.getTime() * 2.2f, skeleton.getTime() * 2.2f, true, events);


    }

    /**
     * detects whether the player collides with a solid
     * and sets his position depending on the solids
     * position and dimension
     */
    private Vector2 getCollisionPosition() {

        /* --- COLLISION DETECTION --- */

        // helper variables
        float qX = x + vX;
        float qY = y + vY;

        float nX;
        float nY;


        _pt.set(getTileCollisionPosition(x, y, vX, vY));
        nX = _pt.x;
        nY = _pt.y;


        vec2.set(nX, nY);
        return vec2;
    }

    private Vector2 getObjectCollisionPosition(float pX, float pY, float vX, float vY) {
        float _x = pX + vX;
        float _y = pY + vY;

        Array<Rectangle> r;

        // left
        if (vX < 0) {
            r = map.getCollisionObjects(pX + vX, pY, pX + vX, pY + height);
            if (r.size != 0) {
                _x = map.getXmax(r) + 0.001f;
                System.out.println("collision");
            }
        }

        // right
        else if (vX > 0) {
            r = map.getCollisionObjects(pX + vX + width, pY, pX + vX + width, pY + height);
            if (r.size != 0) {
                _x = map.getXmin(r) - width - 0.001f;
                System.out.println("collision");
            }
        }


        // top
        if (vY > 0) {
            r = map.getCollisionObjects(pX, pY + height + vY, pX + width, pY + height + vY);
            if (r.size != 0) {
                System.out.println("top");
                _y = map.getYmin(r) - height - 0.001f;
            }
        }

        // bottom
        else if (vY < 0) {
            r = map.getCollisionObjects(pX, pY + vY, pX + width, pY + vY);
            if (r.size != 0) {
                _y = map.getYmax(r) + 0.001f;
                System.out.println("collision");
            }
        }

        vec2.set(_x, _y);
        return vec2;
    }

    private Vector2 getTileCollisionPosition(float pX, float pY, float vX, float vY) {

        float _x = pX;
        float _y = pY;

        float qX = pX + vX;
        float qY = pY + vY;

        /* COLLIDED TILES */

        /* X-AXIS */
        if (vX < 0) {
            // botton left
            if (map.isSolid(qX, pY)) {
                _x = ((int) (pX) / map.getTileWidth()) * map.getTileWidth();
            }
            // top left
            else if (map.isSolid(qX, pY + height)) {
                _x = ((int) (pX) / map.getTileWidth()) * map.getTileWidth();
            } else {
                _x = qX;
            }
        } else if (vX > 0)
            // bottom right
            if (map.isSolid(qX + width, pY)) {
                _x = ((int) (qX) / map.getTileWidth()) * map.getTileWidth() - epsilon;
            }
            // top right
            else if (map.isSolid(qX + width, pY + height)) {
                _x = ((int) (qX) / map.getTileWidth()) * map.getTileWidth() - epsilon;
            } else {
                _x = qX;
            }

        /* Y_AXIS */
        if (vY < 0) {
            // bottom left
            if (map.isSolid(pX, qY)) {
                _y = ((int) (pY) / map.getTileHeight()) * map.getTileHeight();
            }
            // bottom right
            else if (map.isSolid(pX + width, qY)) {
                _y = ((int) (pY) / map.getTileHeight()) * map.getTileHeight();
            } else {
                _y = qY;
            }
        } else if (vY > 0) {
            // top left
            if (map.isSolid(pX, qY + height)) {
                _y = ((int) (qY) / map.getTileHeight()) * map.getTileHeight() - epsilon;
            }
            // top right
            else if (map.isSolid(pX + width, qY + height)) {
                _y = ((int) (qY) / map.getTileHeight()) * map.getTileHeight() - epsilon;
            } else {
                _y = qY;
            }
        }

        vec2.set(_x, _y);
        return vec2;
    }

    private Vector2 getMapCollisionPosition(float pX, float pY, float vX, float vY) {
        // helper variables
        float qX = pX + vX;
        float qY = pY + vY;

       /* MAP COLLISION */
        if (qX < map.getX() + map.borderWidth)
            qX = map.getX() + map.borderWidth;
        else if (qX + width > map.getX() + map.getWidth() - map.borderWidth)
            qX = map.getX() + map.getWidth() - map.borderWidth - width;
        if (qY > map.getHeight() - map.borderWidth - 64)// map.getOffsetX() * map.getTileHeight())
            qY = map.getHeight() - map.borderWidth - 64;//map.getOffsetX() * map.getTileHeight();
        else if (qY < map.getY() + map.borderWidth + 64)
            qY = map.getY() + map.borderWidth + 64;

        vec2.set(qX, qY);
        return vec2;
    }


    private void drawRectangularShape() {
        // draw rectangular shape
        Pixmap p = new Pixmap((int) (width), (int) (height), Pixmap.Format.RGBA8888);
        Texture t = new Texture(p.getWidth(), p.getHeight(), Pixmap.Format.RGBA8888);

        p.setColor(0, 0, 0, 1);
        p.fillRectangle(0, 0, (int) width, (int) height);

        t.draw(p, 0, 0);
    }

}
