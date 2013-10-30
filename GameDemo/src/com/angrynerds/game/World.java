package com.angrynerds.game;

import com.angrynerds.camera.CameraHelper.CameraHelper;
import com.angrynerds.gameobjects.Map;
import com.angrynerds.gameobjects.Player;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * User: Franjo
 * Date: 27.10.13
 * Time: 12:49
 * Project: GameDemo
 */
public class World {

    public Map map;
    private Player player;

    public OrthographicCamera camera;
    public CameraHelper cameraHelper;

    public World(OrthographicCamera camera){
        System.out.println("World");
        this.camera = camera;

        init();
    }

    private void init() {
        cameraHelper = new CameraHelper(this);
        cameraHelper.applyTo(camera);

        player = new Player(camera,this);
        map = new Map(this, player);


        cameraHelper.setTarget(player);
    }


    public void update(float deltaTime) {
        map.update(deltaTime);

        cameraHelper.update(deltaTime);
        cameraHelper.applyTo(camera);

    }

    public void render(SpriteBatch batch) {
//        batch.begin();
        map.render(batch);
//        batch.end();
    }
}
