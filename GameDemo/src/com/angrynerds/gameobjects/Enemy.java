package com.angrynerds.gameobjects;

import com.angrynerds.ai.pathfinding.AStarPathFinder;
import com.angrynerds.ai.pathfinding.ClosestHeuristic;
import com.angrynerds.ai.pathfinding.Path;
import com.angrynerds.game.World;
import com.angrynerds.game.screens.play.PlayScreen;
import com.angrynerds.gameobjects.creatures.Creature;
import com.angrynerds.gameobjects.creatures.Goblin;
import com.angrynerds.input.TouchInput;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;

import java.util.Random;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * User: Basti
 * Date: 02.11.13
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
public class Enemy extends Creature {


    private Map map;
    private Player player;
    private Random random = new Random();
    int ranX;
    int ranY;
    boolean reached;

    private Path path;
    private AStarPathFinder pathFinder;
    private int nextStepInPath = 1;


    public int health;
    public int type;

    Vector2 nextStep = new Vector2();
    float angle;



    private Vector2 velocity = new Vector2();
    private int speed = 120;
    private float tolerance =1.0f;
    private boolean alive  = true;

    private Animation ani;

    private int xTilePosition;
    private int yTilePosition;
    private int xTilePlayer;
    private int yTilePlayer;


    public Enemy(String name, String path, String skin, Player player, float scale, int health, int type) {
        super(name, path, skin, scale);

        this.player = player;
        this.health = health;
        this.type = type;
    }

    public Enemy(float x, float y,String name, String path, String skin, Player player, float scale) {
        super(name, path, skin, scale);
        this.x = x;
        this.y = y;

        this.player = player;

        init();
    }


    public void init() {


        map = Map.getInstance();
        pathFinder = AStarPathFinder.getInstance();

        ani = skeletonData.findAnimation("move");
        updatePositions();
        path = getNewPath();
        ranX = -1 + (int) ( + (Math.random() *3)  );
        ranY = -1 + (int) ( + (Math.random() *3)  );
        System.out.println(ranX);





    }


    public void render(SpriteBatch batch) {
        super.render(batch);



    }


    public void update(float deltatime) {
        super.update(deltatime);
        path = getNewPath();



        if (alive){
         updatePositions();

            if(path != null && path.getLength()>2)
                 moveToPlayer(deltatime);
            else   {
                ani = skeletonData.findAnimation("attack");
                ani.mix( skeleton, skeleton.getTime(), skeleton.getTime(), true, null,1);

            }
        }

        else {
            ani = skeletonData.findAnimation("die");
            ani.mix( skeleton, skeleton.getTime(), skeleton.getTime(), false, null,1);
        }

    }

    public void updatePositions(){

        xTilePosition =   (int) (x) / map.getTileWidth();
        yTilePosition =   (int) (y) / map.getTileHeight();
        xTilePlayer   =   (int) (player.x) / map.getTileWidth() + ranX;
        yTilePlayer   =   (int) (player.y) / map.getTileHeight() + ranY ;

    }

    public Path getNewPath(){
               //updatePositions();
        return pathFinder.findPath(1,xTilePosition ,yTilePosition ,xTilePlayer  , yTilePlayer );
    }

    public int getTilePostionX(){

            return xTilePosition;
    }

    public int getTilePostionY(){

        return yTilePosition;
    }


    public void moveToPlayer(float deltatime){

       if (alive){

        skeleton.setFlipX((player.x - x >= 0));


        if (path != null && nextStepInPath < path.getLength()) {
            attack();

            nextStep = new Vector2((float) path.getStep(nextStepInPath).getX() * map.getTileWidth(), (float) path.getStep(nextStepInPath).getY() * map.getTileHeight());
            angle = (float) Math.atan2(path.getStep(nextStepInPath).getY() * map.getTileHeight() - y, path.getStep(nextStepInPath).getX() * map.getTileWidth() - x);
            velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);



                if ((int) x  != nextStep.x) {
                    x = x + velocity.x * deltatime;
                    System.out.println(x + "      " + nextStep.x);
                     }


                if ((int)y != nextStep.y ){

                    y = (y + velocity.y * deltatime);
                     }




            if(path.getLength()-1 == 1){
                ani =  skeletonData.findAnimation("attack");

            }
            else
                ani  = skeletonData.findAnimation("move");

        }
        }


        ani.apply(skeleton, skeleton.getTime(), skeleton.getTime(), true, null);


    }




    public void hit(int healthDecrease){
        health -= healthDecrease;
        if(health <= 0)   {
           die();


        }

    }
    public void die(){

        alive = false;
    }

    private boolean isReached(int i) {



        return Math.abs(path.getStep(i).getX() * map.getTileWidth() - getX()) <= speed / tolerance * Gdx.graphics.getDeltaTime() &&
                Math.abs(path.getStep(i).getY() * map.getTileHeight() - getY()) <= speed / tolerance * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void attack() {

    }

}
