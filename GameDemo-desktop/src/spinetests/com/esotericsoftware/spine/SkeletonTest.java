package spinetests.com.esotericsoftware.spine; /******************************************************************************
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


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;

public class SkeletonTest extends ApplicationAdapter {
	SpriteBatch batch;
	float time;
	SkeletonRenderer renderer;
	SkeletonRendererDebug debugRenderer;

	SkeletonData skeletonData;
	Skeleton skeleton;
	Animation animation;
	Array<Event> events = new Array();

	public void create () {
		batch = new SpriteBatch();
		renderer = new SkeletonRenderer();
		debugRenderer = new SkeletonRendererDebug();

		final String name = "goblins"; // "spineboy";

		// A regular texture atlas would normally usually be used. This returns a white image for images not found in the atlas.
		Pixmap pixmap = new Pixmap(32, 32, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		final AtlasRegion fake = new AtlasRegion(new Texture(pixmap), 0, 0, 32, 32);
		pixmap.dispose();

//        FileHandle h = Gdx.files.internal("raw/");


        System.out.println("name:" + name);
        System.out.println(Gdx.files.internal("/raw").path());
//        FileHandle atlasFile = Gdx.files.internal(name + ".atlas");
        FileHandle atlasFile = Gdx.files.internal(name + ".atlas");//Gdx.files.internal(name + ".atlas");
//        FileHandle atlasFile = Gdx.files.absolute("D:\Dropbox\_dev\_medienprojekt\GameDemo\GameDemo-desktop\src\spinetests\com\esotericsoftware\spine/name + ".atlas");
		TextureAtlasData data = !atlasFile.exists() ? null : new TextureAtlasData(atlasFile, atlasFile.parent(), false);
		TextureAtlas atlas = new TextureAtlas(data) {
			public AtlasRegion findRegion (String name) {
				AtlasRegion region = super.findRegion(name);
				return region != null ? region : fake;
			}
		};

        System.out.println(atlas.findRegion("goblin").name);

		if (true) {
			System.out.println("JSON");
			SkeletonJson json = new SkeletonJson(atlas);
			// json.setScale(2);
			skeletonData = json.readSkeletonData(Gdx.files.internal(name + ".json"));
		} else {
			System.out.println("Binary");
			SkeletonBinary binary = new SkeletonBinary(atlas);
			// binary.setScale(2);
			skeletonData = binary.readSkeletonData(Gdx.files.internal(name + ".skel"));
		}
		animation = skeletonData.findAnimation("walk");

		skeleton = new Skeleton(skeletonData);

		if (name.equals("goblins")) skeleton.setSkin("goblin");
		skeleton.setToSetupPose();
		skeleton = new Skeleton(skeleton);
		skeleton.updateWorldTransform();

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDown (int screenX, int screenY, int pointer, int button) {
				keyDown(0);
				return true;
			}

			public boolean keyDown (int keycode) {
				if (name.equals("goblins")) {
					skeleton.setSkin(skeleton.getSkin().getName().equals("goblin") ? "goblingirl" : "goblin");
					skeleton.setSlotsToSetupPose();
				}
				return true;
			}
		});
	}

	public void render () {
		float lastTime = time;
		time += Gdx.graphics.getDeltaTime();

		float x = skeleton.getX() + 160 * Gdx.graphics.getDeltaTime() * (skeleton.getFlipX() ? -1 : 1);
		if (x > Gdx.graphics.getWidth()) skeleton.setFlipX(true);
		if (x < 0) skeleton.setFlipX(false);
		skeleton.setX(x);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		events.clear();
		animation.apply(skeleton, lastTime, time, true, events);
		if (events.size > 0) System.out.println(events);

		skeleton.updateWorldTransform();
		skeleton.update(Gdx.graphics.getDeltaTime());

		batch.begin();
		renderer.draw(batch, skeleton);
		batch.end();

		debugRenderer.draw(skeleton);
	}

	public void resize (int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		debugRenderer.getShapeRenderer().setProjectionMatrix(batch.getProjectionMatrix());
	}

	public static void main (String[] args) throws Exception {
		new LwjglApplication(new SkeletonTest());
	}
}
