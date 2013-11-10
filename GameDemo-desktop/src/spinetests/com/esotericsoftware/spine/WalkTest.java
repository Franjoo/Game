/******************************************************************************
 * Spine Runtime Software License - Version 1.1
 *
 * Copyright (c) 2013, Esoteric Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms in whole or in part, with
 * or without modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. A Spine Essential, Professional, Enterprise, or Education License must
 *    be purchased from Esoteric Software and the license must remain valid:
 *    http://esotericsoftware.com/
 * 2. Redistributions of source code must retain this license, which is the
 *    above copyright notice, this declaration of conditions and the following
 *    disclaimer.
 * 3. Redistributions in binary form must reproduce this license, which is the
 *    above copyright notice, this declaration of conditions and the following
 *    disclaimer, in the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

package spinetests.com.esotericsoftware.spine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;

public class WalkTest extends ApplicationAdapter {
    SpriteBatch batch;
    float time;
    float walktime;
    float lastTime;
    Array<Event> events = new Array();

    SkeletonRenderer renderer;
    SkeletonRendererDebug debugRenderer;

    SkeletonData skeletonData;
    Skeleton skeleton;
    Animation walkAnimation;
    Animation jumpAnimation;

    private float vX_max = 1000;

    public void create() {
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        debugRenderer = new SkeletonRendererDebug();

        final String name = "spineboy";

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(name + ".atlas"));

        if (true) {
            SkeletonJson json = new SkeletonJson(atlas);
            // json.setScale(2);
            skeletonData = json.readSkeletonData(Gdx.files.internal(name + ".json"));
        } else {
            SkeletonBinary binary = new SkeletonBinary(atlas);
            // binary.setScale(2);
            skeletonData = binary.readSkeletonData(Gdx.files.internal(name + ".skel"));
        }

//        skeletonData.

        walkAnimation = skeletonData.findAnimation("walk");
        jumpAnimation = skeletonData.findAnimation("jump");

        for (int i = 0; i < skeletonData.getAnimations().size; i++) {
            System.out.println(skeletonData.getAnimations().get(i).getName());
        }

        skeleton = new Skeleton(skeletonData);
        skeleton.updateWorldTransform();
        skeleton.setX(-50);
        skeleton.setY(20);
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime() * 0.25f; // Reduced to make mixing easier to see.

        float jump = jumpAnimation.getDuration();
        float beforeJump = 1f;
        float blendIn = 0.4f;
        float blendOut = 0.4f;
        float blendOutStart = beforeJump + jump - blendOut;
        float total = 3.75f;

        time += delta;

        update(delta);




        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

//        // This shows how to manage state manually. See AnimationStatesTest.
//        if (time > total) {
//            // restart
//            time = 0;
//            skeleton.setX(-50);
//        } else if (time > beforeJump + jump) {
//            // just walk after jump
//            walkAnimation.apply(skeleton, time, time, true, events);
//        } else if (time > blendOutStart) {
//            // blend out jump
//            walkAnimation.apply(skeleton, time, time, true, events);
//            jumpAnimation.mix(skeleton, time - beforeJump, time - beforeJump, false, events, 1 - (time - blendOutStart) / blendOut);
//        } else if (time > beforeJump + blendIn) {
//            // just jump
//            jumpAnimation.apply(skeleton, time - beforeJump, time - beforeJump, false, events);
//        } else if (time > beforeJump) {
//            // blend in jump
//            walkAnimation.apply(skeleton, time, time, true, events);
//            jumpAnimation.mix(skeleton, time - beforeJump, time - beforeJump, false, events, (time - beforeJump) / blendIn);
//        } else {
//            // just walk before jump
//            walkAnimation.apply(skeleton, time, time, true, events);
//        }


//        skeleton.updateWorldTransform();
//        skeleton.update(Gdx.graphics.getDeltaTime());
//        skeleton.getTime()

        batch.begin();
        renderer.draw(batch, skeleton);
        batch.end();

//        debugRenderer.draw(skeleton);
    }

    private void update(float deltaTime) {

       // left
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            skeleton.setX(skeleton.getX() + vX_max * deltaTime);

            if(skeleton.getFlipX())skeleton.setFlipX(false);


//            walktime += deltaTime;
//            System.out.println(skeleton.getTime());

            skeleton.updateWorldTransform();
            skeleton.update(Gdx.graphics.getDeltaTime());

            walkAnimation.apply(skeleton, skeleton.getTime(), skeleton.getTime() + deltaTime, true, events);

//            float speed = 360;
//            if (time > beforeJump + blendIn && time < blendOutStart) speed = 360;

//            System.out.println(walkAnimation.);

        }

        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            skeleton.setX(skeleton.getX() - vX_max * deltaTime);

//            walktime += deltaTime;
//            System.out.println(skeleton.getTime());

            if(!skeleton.getFlipX())skeleton.setFlipX(true);
            skeleton.updateWorldTransform();
            skeleton.update(Gdx.graphics.getDeltaTime());

            walkAnimation.apply(skeleton, deltaTime, skeleton.getTime() + deltaTime, true, events);

//            float speed = 360;
//            if (time > beforeJump + blendIn && time < blendOutStart) speed = 360;

//            System.out.println(walkAnimation.);

        }

        else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            skeleton.updateWorldTransform();
            skeleton.update(Gdx.graphics.getDeltaTime());



//            walkAnimation.apply(skeleton, skeleton.getTime(), skeleton.getTime() + deltaTime, true, events);
                        jumpAnimation.apply(skeleton, 0, skeleton.getTime() + deltaTime, false, events);

        }


    }

    public void resize(int width, int height) {

        int w = Gdx.graphics.getHeight() / height * width;
        int h = height;

//        batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);


        debugRenderer.getShapeRenderer().setProjectionMatrix(batch.getProjectionMatrix());
    }

    public static void main(String[] args) throws Exception {
        new LwjglApplication(new WalkTest());
    }
}
