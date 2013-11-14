package com.angrynerds.gameobjects;

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
public class Player extends GameObject {
    private static final String TAG = Player.class.getSimpleName();

    // constants
    private final float epsilon = Constants.EPSILON;

    // map
    private Camera camera;
    private Map map;

    // movement
    private float vX;
    private float vY;
    private float vX_MAX = 180;
    private float vY_MAX = 120;

    // helper attributes
    private Vector2 vec2 = new Vector2();
    private Array<Vector2> collPos = new Array<Vector2>();

    private Vector2 _po = new Vector2();
    private Vector2 _pm = new Vector2();
    private Vector2 _pt = new Vector2();

    // spine relevant attributes
    private SkeletonRenderer skeletonRenderer;
    private SkeletonRendererDebug skeletonDebugRenderer;
    Array<Event> events = new Array();

    private SkeletonData skeletonData;
    private Skeleton skeleton;
    private Animation walkAnimation;
    private Animation jumpAnimation;

    private float lastTime = 0;
    private AnimationState state;

    // input
    private IGameInputController input;


    /**
     * creates a new player with assigned camera used for input ui
     *
     * @deprecated
     */
    public Player(Camera camera) {
        super();

        this.camera = camera;
    }

    /**
     * creates a new player
     */
    public Player(IGameInputController input) {
        super();

        this.input = input;
    }

    public void init() {

        map = Map.getInstance();

        x = map.getSpawn().x;
        y = map.getSpawn().y;

        width = 32;
        height = 32;

//        setPosition(x, y);
//        setSize(width, height);
//
//
//        setOrigin(0, 0);

        // draw rectangular shape
        Pixmap p = new Pixmap((int) (width), (int) (height), Pixmap.Format.RGBA8888);
        Texture t = new Texture(p.getWidth(), p.getHeight(), Pixmap.Format.RGBA8888);

        p.setColor(0, 0, 0, 1);
        p.fillRectangle(0, 0, (int) width, (int) height);
        p.setColor(1, 1, 1, 1);
//        p.fillRectangle((int) origin.x, (int) origin.y, 5, 5);
        p.setColor(1, 0, 0, 1);
        p.drawLine((int) x, (int) y, (int) width + 20, (int) y);
        p.drawLine((int) x, (int) y, (int) (x), (int) height + 20);

        t.draw(p, 0, 0);

//       setTexture(t);


//        set input processor
//        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
//            Array controllers = Controllers.getControllers();
//            if (controllers.size != 0) {
//                input = new X360Gamepad((Controller) controllers.get(X360Gamepad.NUM_CONTROLLERS));
//            } else {
////                input = new TouchInput();
////                input = new KeyboardInput();
////                input = new DeprecatedTouchInput(camera);
//            }
//        } else if (Gdx.app.getType() == Application.ApplicationType.Android) {
//            input = new DeprecatedTouchInput(camera);
//        }


        // some basic init assertions
        assert (input != null) : (TAG + ": input must not be null");

        System.out.println("DIMENSION:" + width + " " + height);


        // skeleton
        skeletonRenderer = new SkeletonRenderer();
        skeletonDebugRenderer = new SkeletonRendererDebug();

        final String name = "spineboy";

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(name + ".atlas"));


        if (true) {
            SkeletonJson json = new SkeletonJson(atlas);

            // set skeleton and images scaled
            json.setScale(0.7f);

            skeletonData = json.readSkeletonData(Gdx.files.internal(name + ".json"));
        } else {
            SkeletonBinary binary = new SkeletonBinary(atlas);
            // binary.setScale(2);
            skeletonData = binary.readSkeletonData(Gdx.files.internal(name + ".skel"));
        }

        AnimationStateData stateData = new AnimationStateData(skeletonData);
        stateData.setMix("walk", "jump", 0.2f);
        stateData.setMix("jump", "walk", 0.4f);

        state = new AnimationState(stateData);
        state.setAnimation(0, "walk", true);

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
                if (state.getCurrent(trackIndex).getAnimation().equals("jump")) {
                    System.out.println("walk completed--------------------");
                }
            }
        });


        walkAnimation = skeletonData.findAnimation("walk");
        jumpAnimation = skeletonData.findAnimation("jump");

//        for (int i = 0; i < skeletonData.getAnimations().size; i++) {
//            System.out.println(skeletonData.getAnimations().get(i).getName());
//        }

        skeleton = new Skeleton(skeletonData);


        skeleton.updateWorldTransform();
        skeleton.setX(x);
        skeleton.setY(y);

        System.out.println("skeleton time: " + skeleton.getTime());


    }

    //    @Override
    public void render(SpriteBatch batch) {

        batch.begin();
//        batch.draw(getTexture(), x, y);
        batch.end();

        batch.begin();
        skeletonRenderer.draw(batch, skeleton);
        batch.end();

//        if (Gdx.app.getType() == Application.ApplicationType.Android) {
//            DeprecatedTouchInput i = (DeprecatedTouchInput) input;
//            i.ui.render(batch);
//        }

    }

    //    @Override
    public void update(float deltaTime) {

        // set v in x and y direction
        vX = input.get_stickX() * deltaTime * vX_MAX;
        vY = input.get_stickY() * deltaTime * vY_MAX;

        // set collision position
        Vector2 p = getCollisionPosition();

        // update position attributes
        x = p.x;
        y = p.y;

        // set position after setting collsion position
//        setPosition(x, y);

        //*** SKELETON ***//


//        X360Gamepad gp = (X360Gamepad) input;
//        float walkspeed = gp.stick_left_intensity();
        float walkspeed = 1;

        state.apply(skeleton);
        skeleton.updateWorldTransform();


        if (walkspeed != 0) {
            // flip
            if (vX < 0) skeleton.setFlipX(true);
            else skeleton.setFlipX(false);

            // position
            skeleton.setX(x);
            skeleton.setY(y);

            state.update(deltaTime);


//            skeleton.setTime(skeleton.getTime());
//
//            // update skeleton
//            skeleton.updateWorldTransform();
//            skeleton.update(Gdx.graphics.getDeltaTime());
//
//            walkAnimation.apply(skeleton, skeleton.getTime(), skeleton.getTime(), true, events);

        }
        // jump
        if (input.get_isA()) {

            System.out.println("a pressed");
            state.setAnimation(0, "jump", false); // Set animation on track 0 to jump.
            state.addAnimation(0, "walk", true, 0); // Queue walk to play after jump.

//            state.getCurrent(0).

//                    state.update(deltaTime);

        }

        if (state.getCurrent(0).getAnimation().getName().equals("jump")) {
            state.update(deltaTime);
            System.out.println("jump");
            if (state.getCurrent(0).isComplete()) {
                System.out.println("complete");
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            skeleton.setX(skeleton.getX() + vX_MAX * deltaTime);

            if (skeleton.getFlipX()) skeleton.setFlipX(false);


//            walktime += deltaTime;
//            System.out.println(skeleton.getTime());

            skeleton.updateWorldTransform();
            skeleton.update(Gdx.graphics.getDeltaTime());

//            walkAnimation.apply(skeleton, skeleton.getTime(), skeleton.getTime() + deltaTime, true, events);

//            float speed = 360;
//            if (time > beforeJump + blendIn && time < blendOutStart) speed = 360;

//            System.out.println(walkAnimation.);

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            skeleton.setX(skeleton.getX() - vX_MAX * deltaTime);

//            walktime += deltaTime;
//            System.out.println(skeleton.getTime());

            if (!skeleton.getFlipX()) skeleton.setFlipX(true);
            skeleton.updateWorldTransform();
            skeleton.update(Gdx.graphics.getDeltaTime());

            walkAnimation.apply(skeleton, deltaTime, skeleton.getTime() + deltaTime, true, events);

//            float speed = 360;
//            if (time > beforeJump + blendIn && time < blendOutStart) speed = 360;

//            System.out.println(walkAnimation.);

        }


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

        // _pm.x,_pm.y, qX - _pm.x, qY - _pm.y
        // x, y, vX, vY

        _pt.set(getTileCollisionPosition(x, y, vX, vY));
        nX = _pt.x;
        nY = _pt.y;

//        _pm.set(getMapCollisionPosition(_po.x,_po.y, qX - _po.x, qY - _po.y));
//        nX = _pm.x;
//        nY = _pm.y;

//        Vector2 _po = getObjectCollisionPosition(_pm.x,_pm.y, qX - _pm.x, qY - _pm.y);
//        nX = _po.x;
//        nY = _po.y;
//        Vector2 _po = getObjectCollisionPosition(x, y, vX, vY);
//        Vector2 _pt = getTileCollisionPosition(x, y, vX, vY);
//        System.out.println(position + "  " + _pm);

//        Vector2 _p = _pm;

//        if(vX > 0){
//           if(_pm.x < _po.x && _pm.x < _pt.x) nX = _pm.x;
//           else if(_po.x < _pm.x && _po.x < _pt.x) nX = _po.x;
//           else (_pt.x < _po.x && _pt.x < _pm.x) nX = _pt.x;
//        }
//        else if( vY > 0){
//
//        }


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
