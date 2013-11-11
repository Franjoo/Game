package com.angrynerds.gameobjects.creatures;

import com.angrynerds.gameobjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;

/**
 * User: Franjo
 * Date: 07.11.13
 * Time: 14:38
 * Project: GameDemo
 */
public class Creature extends GameObject {
    // debug attributes
    public boolean showBounds;

    // skeleton
    protected SkeletonData skeletonData;
    protected Skeleton skeleton;
    private SkeletonJson skeletonJson;

    // renderer
    protected SkeletonRenderer skeletonRenderer;
    protected SkeletonRendererDebug skeletonDebugRenderer;

    // constructor params
    private String name;
    private TextureAtlas atlas;
    private float scale;
    private String skin;


    public Creature(String name, TextureAtlas atlas, String skin, float scale) {
        this.atlas = atlas;
        this.name = name;
        this.scale = scale;
        this.skin = skin;

        create();
    }

    public Creature(String name, TextureAtlas atlas, String skin) {
        this(name, atlas, skin, 1);
    }


    private void create() {
        // skeleton json
        skeletonJson = new SkeletonJson(atlas);

        // set scale
        skeletonJson.setScale(scale);

        // get skeletonData
        skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(name + ".json"));

        // create skeleton
        skeleton = new Skeleton(skeletonData);

        if (skin != null) {
            // set skin
            skeleton.setSkin(skin);
            skeleton.setToSetupPose();

            // recreate skeleton with skin
            skeleton = new Skeleton(skeleton);
            skeleton.updateWorldTransform();
        }

        // create renderer
        skeletonRenderer = new SkeletonRenderer();
        skeletonDebugRenderer = new SkeletonRendererDebug();
    }

    public void update(float deltaTime) {
        // set skeleton position
        skeleton.x = x;
        skeleton.y = y;

        // update state
        skeleton.updateWorldTransform();
        skeleton.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        // draw skeleton
        batch.begin();
        skeletonRenderer.draw(batch, skeleton);
        batch.end();

        // draw bounds
        if (showBounds) skeletonDebugRenderer.draw(skeleton);
    }

}
