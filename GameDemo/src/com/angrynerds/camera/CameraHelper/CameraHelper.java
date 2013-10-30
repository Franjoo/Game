package com.angrynerds.camera.CameraHelper;

import com.angrynerds.game.PlayController;
import com.angrynerds.game.World;
import com.angrynerds.gameobjects.GameObject;
import com.angrynerds.gameobjects.Map;
import com.angrynerds.gameobjects.Player;
import com.angrynerds.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {
    public static final String TAG = CameraHelper.class.getSimpleName();

    private static final float MAX_ZOOM_IN = 0.25f;
    private static final float MAX_ZOOM_OUT = 4.0f;

    private float aX = 0.03f;
    private float aY = 0.1f;
//    private float aX = 1;
//    private float aY = 1;

    private Vector2 position;
    private float zoom;
    private Sprite target;
    private Map map;

    private World world;

    private PlayController playController;
    public float deltaX;
    public float deltaY;

    public CameraHelper(World world) {
        this.playController = playController;

//        map = playController.world.map;
        position = new Vector2();
        zoom = 1;

    }

    public void update(float deltaTime) {

        handleDebugControlls();

        if (!hasTarget()){
            return;
        }


        float qX = target.getX();
        float qY = target.getY();

        deltaX = qX - position.x;
        deltaY = qY - position.y;


//        if(Math.abs(deltaX) < 0.03){
//            deltaX = 0;
//        }
//
//        if(Math.abs(deltaY) < 0.03){
//            deltaY = 0;
//        }

//        if (!(qX < Constants.VIEWPORT_WIDTH / 2)) {
//            position.x = target.getX() + target.getOriginX();
        position.x += deltaX * aX;

//        }

//        if (!(qY < Constants.VIEWPORT_HEIGHT / 2 + 2 * 32)) {
//            position.y = target.getY() + target.getOriginY();                                4
        position.y += deltaY * aY;

//        }

//        position.x = target.getX() + target.getOriginX();
//        position.y = target.getY() + target.getOriginY();


    }

    private void handleDebugControlls() {
        // Zoom in
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) {
            addZoom(-0.006f);
            if (getZoom() < MAX_ZOOM_IN) setZoom(MAX_ZOOM_IN);
            System.out.println("Zoom: " + getZoom());
        }

        // Zoom out
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
            addZoom(0.006f);
            if (getZoom() > MAX_ZOOM_OUT) setZoom(MAX_ZOOM_OUT);
            System.out.println("Zoom: " + getZoom());
        }

        // set Zoom 1
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3)) {
            setZoom(1);
            System.out.println("Zoom: " + getZoom());
        }
    }


    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public float getZoom() {
        return zoom;
    }

    public void setTarget(Sprite target) {
        this.target = target;
    }

    public Sprite getTarget() {
        return target;
    }

    public boolean hasTarget(Sprite target) {
        return hasTarget() && this.target.equals(target);
    }

    public boolean hasTarget() {
        return target != null;
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

}
