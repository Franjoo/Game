package com.angrynerds.game;

import com.angrynerds.gameobjects.creatures.Creature;
import com.angrynerds.gameobjects.creatures.Boy;
import com.angrynerds.gameobjects.creatures.Goblin;
import com.angrynerds.gameobjects.creatures.Max;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.FloatArray;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;

/**
 * User: Franjo
 * Date: 07.11.13
 * Time: 22:19
 * Project: GameDemo
 */
public class CreatureTest extends ApplicationAdapter {
    // creatures
    Creature goblin;
    Creature boy;
    Creature max;

    private float vX;
    private float vY;
    private float vX_MAX = 320;
    private float vY_MAX = 220;

    // batch
    SpriteBatch batch;

    public void create() {

        // batch
        batch = new SpriteBatch();

        // params
        String name;
        String path;
        float scale;
        String skin;

        // set up goblin
        name = "goblins";
        path = "C:/Users/Basti/Game/GameDemo-android/assets/data/spine/goblins/";
        scale = 1;
        skin = "goblin";
        goblin = new Goblin(name, path, skin, scale);




        // set up boy
        name = "spineboy";
        path = "C:/Users/Basti/Game/GameDemo-android/assets/data/spine/spineboy/";
        scale = 1;
        skin = null;
        boy = new Boy(name, path, skin, scale);

        // set up max
        name = "Max_move";
        path = "C:/Users/Basti/Game/GameDemo-android/assets/data/spine/max/";
        scale = 0.4f;
        skin = null;
        max = new Max(name, path, skin, scale);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

//        // render goblin
        goblin.update(Gdx.graphics.getDeltaTime());
        goblin.render(batch);
//
//        // render boy
        boy.update(Gdx.graphics.getDeltaTime());
        boy.render(batch);

        // render max
       // max.update(Gdx.graphics.getDeltaTime());
       // max.render(batch);
        this.update();
    }

    public void update(){

         BoundingBoxAttachment gbb1 =  goblin.getSkeletonBounds().getBoundingBoxes().get(0);
         BoundingBoxAttachment gbb2 = (  goblin.getSkeletonBounds().getBoundingBoxes().get(1));

        Polygon pol;
        FloatArray fa =   goblin.getSkeletonBounds().getPolygon(goblin.getSkeletonBounds().getBoundingBoxes().get(1));
        float[] fa2 = fa.toArray();
        pol = new Polygon( fa2);
        System.out.println(pol.getBoundingRectangle().toString());
        //  System.out.println(  boy.getSkeletonBounds().getBoundingBoxes().get(0).getName());
       // System.out.println(  boy.getSkeletonBounds().getBoundingBoxes().get(1).getName());

        if(goblin.getSkeletonBounds().aabbIntersectsSkeleton(boy.getSkeletonBounds()))
            goblin.getSkeletonBounds();
           System.out.println("true");

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public static void main(String[] args) {
        new LwjglApplication(new CreatureTest(), "CreatureTest", 800, 480, true);
    }
}
