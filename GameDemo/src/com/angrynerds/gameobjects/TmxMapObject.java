package com.angrynerds.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * User: Franjo
 * Date: 30.10.13
 * Time: 19:28
 * Project: GameDemo
 */
public class TmxMapObject {
    public Texture texture;
    public TextureRegion region;

    public float x;
    public float y;
    public float width;
    public float height;

    public TmxMapObject(TextureRegion region, float x, float y, float width, float height) {
        this.region = region;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public TmxMapObject(TextureRegion region) {
        this(region, 0, 0, 0, 0);
    }

    public void render(SpriteBatch batch) {
        System.out.println("render");

        batch.begin();
        batch.draw(region, x, y, width, height);
        batch.end();
    }
}
