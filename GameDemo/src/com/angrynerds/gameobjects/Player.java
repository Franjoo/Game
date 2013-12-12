package com.angrynerds.gameobjects;

import com.angrynerds.game.collision.Detector;
import com.angrynerds.gameobjects.creatures.Creature;
import com.angrynerds.input.IGameInputController;
import com.angrynerds.util.C;
import com.angrynerds.util.State;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private final float epsilon = C.EPSILON;

    // map
    private Camera camera;
    private Map map;

    // movement
    private float vX;
    private float vY;
    private float vX_MAX = 320;
    private float vY_MAX = 220;

    // stats
    private int maxHP = 100;
    private int actHP;

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

    private AnimationListener animationListener;

    private Animation currentAnimation;


    private Detector detector;

    // input
    private IGameInputController input;


    /**
     * creates a new player
     */
    public Player(IGameInputController input) {
        super("Max_move", "data/spine/max/", null, 0.20f);


        this.input = input;

//        walkAnimation = skeletonData.findAnimation("run_test");
//        jumpAnimation = skeletonData.findAnimation("jump");


//        showBounds = true;

    }

    public void init() {

        map = Map.getInstance();
        detector = Detector.getInstance();

        x = 500;
        y = 300;

        actHP = maxHP;

        width = 32;
        height = 32;


        setAnimationStates();

    }

    private void setAnimationStates() {
        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
        stateData.setMix("run_test", "jump", 0.6f);
        stateData.setMix("jump", "run_test", 0.5f);
        stateData.setMix("jump", "jump", 0.2f);
        stateData.setMix("run_test", "attack_1", 0.4f);
        stateData.setMix("attack_1", "run_test", 0.4f);


        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        animationListener = new AnimationListener();
        state.addListener(animationListener);
        state.setAnimation(0, "run_test", true);
    }

    public void render(SpriteBatch batch) {
        super.render(batch);
    }







    @Override
    public void attack() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void update(float deltaTime) {
        super.update(deltaTime);


        // set v in x and y direction
        vX = input.get_stickX() * deltaTime * vX_MAX;
        vY = input.get_stickY() * deltaTime * vY_MAX;

        // set collision position
        Vector2 p = getCollisionPosition();

        // update position attributes
        x = p.x;
        y = p.y;

        // flip skeleton
        skeleton.setFlipX(vX < 0);


            setCurrentState();

        // apply and update skeleton
        state.update(deltaTime);
        state.apply(skeleton);

    }


    private void setCurrentState() {
        if (input.getState() == State.JUMPING && !state.getCurrent(0).toString().equals("jump")) {
            state.setAnimation(0, "jump", false);
            state.addAnimation(0, "run_test", true, 0);
//            state.addAnimation(1, "run_test", true, jumpAnimation.getDuration() - 30);
//            state.addAnimation(1, "run_test", false, 0);
        }





        if (input.getState() == State.ATTACKING && !state.getCurrent(0).toString().equals("attack_1")) {
            state.setAnimation(0, "attack_1", false);
            state.addAnimation(0, "run_test", true, 0);
        }
        input.setState(State.IDLE);
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


    private void setTileCollisionPosition() {


        //LEFT
        if (vX < 0) {
            if (detector.isSolid(x, y) || detector.isSolid(x, y + height)) {
                vX = 0;
            }

            // RIGHT
        } else if (vX > 0) {
            if (detector.isSolid(x + width, y) || detector.isSolid(x + width, y + height)) {
                vX = 0;
            }
        }

        // BOTTOM
        if (vY < 0) {
            if (detector.isSolid(x, y) || detector.isSolid(x + width, y)) {
                vY = 0;
            }
        }

        // TOP
        if (vY > 0) {
            if (detector.isSolid(x, y + height) || detector.isSolid(x + width, y + height)) {
                vY = 0;
            }
        }
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
            System.out.println("solid: " + detector.isSolid(x, y));
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

//    public boolean isHit(float x, float y){
////        return
//    }


    public int getMaxHP() {
        return maxHP;
    }

    public int getActualHP() {
        return actHP;
    }

    public void setActualHP(int hp) {
        actHP = hp;
    }

    public void setMaxHP(int hp) {
        maxHP = hp;
    }


    class AnimationListener implements AnimationState.AnimationStateListener {

        @Override
        public void event(int trackIndex, Event event) {
//            System.out.println(trackIndex + " event: " + state.getCurrent(trackIndex) + ", " + event.getData().getName());
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
//            System.out.println(trackIndex + " complete: " + state.getCurrent(trackIndex) + ", " + loopCount);
//            System.out.println(state.getCurrent(trackIndex));
            if (state.getCurrent(trackIndex).toString().equals("jump")) {
                state.setAnimation(0, "run_test", true);
            }
        }

        @Override
        public void start(int trackIndex) {
//            System.out.println(trackIndex + " start: " + state.getCurrent(trackIndex));
        }

        @Override
        public void end(int trackIndex) {
//            System.out.println(trackIndex + " end: " + state.getCurrent(trackIndex));
        }
    }

}
