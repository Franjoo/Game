package com.angrynerds.gameobjects.creatures;

import com.angrynerds.gameobjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;

import java.util.HashMap;
import java.util.Map;

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

    // atlas
    private TextureAtlas atlas;

    // animation map
    private Map<String, Animation> animationMap;

    // constructor params
    private String name;
    private String path;
    private float scale;
    private String skin;


    public Creature(String name, String path, String skin, float scale) {
        this.name = name;
        this.path = path;
        this.scale = scale;
        this.skin = skin;

        create();

        fillAnimationMap();
    }

    public Creature(String name, String path, String skin) {
        this(name, path, skin, 1);
    }

    private void fillAnimationMap() {
        animationMap = new HashMap<String, Animation>();
        for (int i = 0; i < skeletonData.getAnimations().size; i++) {
            Animation animation = skeletonData.getAnimations().get(i);
            System.out.println(animation.getName());
            animationMap.put(animation.getName(), animation);
        }
    }

    protected Animation getAnimation(String name){
          return null;
    }


    private void create() {
        // atlas
        atlas = new TextureAtlas(Gdx.files.internal(path + name + ".atlas"));

        // skeleton json
        skeletonJson = new SkeletonJson(atlas);

        // set scale
        skeletonJson.setScale(scale);


        // get skeletonData
        skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(path + name + ".json"));


        // create skeleton
        skeleton = new Skeleton(skeletonData);

//        skeleton.getDrawOrder().get(i).getData().

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
