package com.angrynerds.game;

import com.angrynerds.game.core.Main;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Desktop Starter Class
 */
public class DesktopStarter {

    private static final String VERSION = "v0.1";

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Ted's Dream " + VERSION;
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;
//        cfg.fullscreen = true;
//        cfg.vSyncEnabled = true;
		
//		new LwjglApplication(new Main(), cfg);
		new LwjglApplication(new Main(), cfg);
	}
}
