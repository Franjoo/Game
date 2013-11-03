package com.angrynerds.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * User: Franjo
 * Date: 30.10.13
 * Time: 19:28
 * Project: GameDemo
 */
public class TmxMapObject {
    public TextureRegion region;

    public int x;
    public int y;
    public int width;
    public int height;
    public Rectangle rectangle;
    public Texture texture;

    public TmxMapObject(TextureRegion region, int x, int y, int width, int height) {
        this.region = region;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        rectangle = new Rectangle(x, y, width, height);
    }

    public TmxMapObject(Texture texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        rectangle = new Rectangle(x, y, width, height);
    }

    public TmxMapObject(TextureRegion region, Rectangle rectangle) {
        this.region = region;
        this.rectangle = rectangle;
    }

    public TmxMapObject(Texture texture, Rectangle rectangle) {
        this.region = region;
        this.rectangle = rectangle;
    }

    public TmxMapObject(TextureRegion region) {
        this(region, 0, 0, 0, 0);
    }



    public void render(SpriteBatch batch) {
//        System.out.println("render");

        batch.begin();

        batch.draw(region, x, y, width, height);
//        batch.dra
        batch.end();
    }
}
