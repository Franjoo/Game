package com.angrynerds.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 23:32
 * Project: GameDemo
 */
public abstract class AbstractScreen implements Screen{

    protected SpriteBatch batch;

    public AbstractScreen(){
        batch = new SpriteBatch();
    }

    @Override
    public void dispose(){
       batch.dispose();
    }

    public abstract void update(float deltaTime);

    public abstract void render(float deltaTime, SpriteBatch batch);

    public abstract void render(float deltaTime);
}
