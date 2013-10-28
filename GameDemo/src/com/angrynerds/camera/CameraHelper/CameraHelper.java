package com.angrynerds.camera.CameraHelper;

import com.angrynerds.game.PlayController;
import com.angrynerds.gameobjects.GameObject;
import com.angrynerds.gameobjects.Player;
import com.angrynerds.util.Constants;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {
    public static final String TAG = CameraHelper.class.getSimpleName();

    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;

    private Vector2 position;
    private float zoom;
    private Sprite target;

    private PlayController playController;

    public CameraHelper(PlayController playController) {
        this.playController = playController;
        position = new Vector2();
        zoom = 1.0f;
    }

    public void update(float deltaTime) {
        if (!hasTarget())
            return;

        float qX = target.getX();
        float qY = target.getY();

        float mX = playController.world.map.position.x;
        float mY = playController.world.map.position.y;
        float mWidth = playController.world.map.dimension.x;
        float mHeight = playController.world.map.dimension.y;

//        if (!(qX < Constants.VIEWPORT_WIDTH / 2) ) {
//            position.x = target.getX() + target.getOriginX();
//        }
//
//        if (!(qY < Constants.VIEWPORT_HEIGHT / 2)) {
//            position.y = target.getY() + target.getOriginY();
//        }

        position.x = target.getX() + target.getOriginX();
        position.y = target.getY() + target.getOriginY();


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
