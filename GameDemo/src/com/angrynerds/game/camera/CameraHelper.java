package com.angrynerds.game.camera;

import com.angrynerds.game.screens.play.PlayController;
import com.angrynerds.game.World;
import com.angrynerds.gameobjects.Map;
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
    public float oldX;
    public float oldY;

    private float targetDeltaX;
    private float targetDeltaY;

    public CameraHelper(World world) {
        this.playController = playController;

//        map = playController.world.map;


        position = new Vector2();
        zoom = 1;

        position.x = Constants.VIEWPORT_WIDTH/2;
        position.y = Constants.VIEWPORT_HEIGHT/2 + 100;


    }

    public void update(float deltaTime) {

        handleDebugControlls();

        if (!hasTarget()){
            return;
        }

        if(map == null){
            map = Map.getInstance();
        }

        float qX = target.getX();
        float qY = target.getY();

        deltaX = qX - position.x;
        deltaY = qY - position.y;

//        System.out.println(deltaX + ", " + deltaY);




        // TOP
        if(qX + deltaX < Constants.VIEWPORT_WIDTH / 2){
            position.x += (Constants.VIEWPORT_WIDTH/2 - position.x) * aX ;
        }else{
            position.x += deltaX * aX;
        }

        // BOTTOM
        if(qY + deltaY - map.getOffsetY() * map.getTileHeight() < Constants.VIEWPORT_HEIGHT / 2){
            position.y += (Constants.VIEWPORT_HEIGHT/2 - position.y + map.getOffsetY() * map.getTileHeight()) * aY ;
        }else{
            position.y += deltaY * aY;
        }

//        position.x = Math.round(position.x);
//        position.y = Math.round(position.y);

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
        position.x = Constants.VIEWPORT_WIDTH/2;
        position.y = Constants.VIEWPORT_HEIGHT/2;
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
        camera.position.x = Math.round(position.x);
        camera.position.y = Math.round(position.y);

//        camera.position.x = (position.x);
//        camera.position.y = (position.y);

        camera.zoom = zoom;
        camera.update();
    }

}
