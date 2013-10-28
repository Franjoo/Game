package com.angrynerds.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;

public class Main implements ApplicationListener {

    // debug bools
    private static final boolean FPS_LOGGING = false;

    private GameController gameController;
    private GameRenderer gameRenderer;

    private FPSLogger fpsLogger;

//    private OrthographicCamera camera;
//    private SpriteBatch batch;
//    private Texture texture;
//    private Sprite sprite;
//
//    private static ControllUI controllUI;
//    private Player player;
//    private Sprite viewportBoder;


    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        gameController = new GameController();
        gameRenderer = new GameRenderer(gameController);

//        float w = Gdx.graphics.getWidth();
//        float h = Gdx.graphics.getHeight();
//
//        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
//        batch = new SpriteBatch();


//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

//		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
//
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);

        //
//        controllUI = new ControllUI(camera, batch);
//        player = new Player();
//
//        Pixmap pixmap = new Pixmap(800, 480, Pixmap.Format.RGBA8888);
//        pixmap.setColor(0, 0, 0, 1);
//        pixmap.drawRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
//
//
//        viewportBoder = new Sprite(new Texture(pixmap));

        fpsLogger = new FPSLogger();
    }

    @Override
    public void dispose() {
       gameRenderer.dispose();
    }

    @Override
    public void render() {

        // background color
        Color c = Color.valueOf("FFFFFF");
        Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gameController.update(Gdx.graphics.getDeltaTime());
        gameRenderer.render();

//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//        viewportBoder.draw(batch);
////        gameRenderer.render(batch);
////		sprite.draw(batch);
////        controllUI.render(batch);
//        player.update(Gdx.graphics.getDeltaTime());
//
//
//        player.render(batch);
//
//        batch.end();
//
//        if (Gdx.app.getType() == ApplicationType.Android) {
//            controllUI.render();
//        }

        if (FPS_LOGGING) fpsLogger.log();

    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

//    public static ControllUI getControllUI() {
//        return controllUI;
//    }
}
