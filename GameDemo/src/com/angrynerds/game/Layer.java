package com.angrynerds.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * User: Franjo
 * Date: 31.10.13
 * Time: 11:28
 * Project: GameDemo
 */
public class Layer {

    private TextureAtlas atlas;

    private Texture text_sky;
    private Texture text_clouds;

    private float cloudsX;

    private Texture texture;
    private Sprite spr_sky;
    private Sprite spr_clouds;

    private World world;

    private Texture ground;
    private Sprite sprite_ground;

    public Layer(World world) {
        this.world = world;

//        texture = new Texture("data/backgrounds/bg.png");
//        TextureRegion r = new TextureRegion(texture);
        spr_sky = new Sprite(new Texture("data/backgrounds/bg.png"));
        spr_sky.setScale(0.4f, 0.4f);

        spr_clouds = new Sprite(new Texture("data/backgrounds/clouds.png"));
        spr_clouds.setSize(spr_sky.getWidth() * spr_sky.getScaleX(), spr_sky.getHeight() * spr_sky.getScaleY());

        ground = new Texture("data/backgrounds/ground_brige_700p.png");
        sprite_ground = new Sprite(ground);

        TextureRegion region_sky = new TextureRegion(new Texture("data/backgrounds/bg.png"));


//        SpriteBatch batch = new SpriteBatch();

        text_sky = new Texture("data/backgrounds/bg.png");
        text_clouds = new Texture("data/backgrounds/clouds.png");


        init();
    }

    private void init() {

    }

    public void render(SpriteBatch batch) {
        batch.begin();

        float mapHeight = 300;
        float scale = 0.5f;

        float delta = Gdx.graphics.getDeltaTime();
        float vX = -10;
//        Texture t = spr_sky.getTexture();

        cloudsX += vX * delta;

        for (int i = 0; i < 20; i++) {
            batch.draw(text_sky, text_sky.getWidth() * i * scale, mapHeight, text_sky.getWidth() * scale, text_sky.getHeight() * scale);
            batch.draw(text_clouds,cloudsX +  text_clouds.getWidth() * i * scale, mapHeight - 180, text_clouds.getWidth() * scale, text_clouds.getHeight() * scale);
        }
//        spr_clouds.setY(mapHeight);
//        spr_clouds.draw(batch);


//        spr_sky.draw(batch);
//        spr_clouds.draw(batch);
        batch.end();
    }

    public void update(float deltaTime) {

    }
}