package com.angrynerds.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * User: Franjo
 * Date: 25.10.13
 * Time: 23:40
 * Project: Main
 */
public abstract class GameObject extends Sprite {
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;

    public GameObject() {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
    }

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

}
