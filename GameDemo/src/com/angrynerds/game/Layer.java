package com.angrynerds.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * User: Franjo
 * Date: 31.10.13
 * Time: 11:28
 * Project: GameDemo
 */
public class Layer extends AbstractLayer {

    private TiledMapTileLayer tl;
    private float scale;
    private boolean useTS;


    public Layer(float x, float y, float vX, float vY, TiledMapTileLayer tl) {
        super(x, y, vX, vY);

        this.tl = tl;
    }

    @Override
    public void update(float deltaTime) {
//        x += vX * deltaTime;
//        y += vY * deltaTime;
    }

    public TiledMapTileLayer getTiledMapTileLayer() {
        return tl;
    }

}