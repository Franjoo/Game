package com.angrynerds.game;

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

    private OrthographicCamera camera;

    public World(OrthographicCamera camera){
        System.out.println("World");
        this.camera = camera;
//        this.player = player;

        init();
    }

    private void init() {
        map = new Map(camera);
    }

    public void update(float deltaTime) {
        map.update(deltaTime);

    }

    public void render(SpriteBatch batch) {
//        batch.begin();
        map.render(batch);
//        batch.end();
    }
}
