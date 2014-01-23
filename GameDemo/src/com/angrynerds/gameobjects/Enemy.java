package com.angrynerds.gameobjects;

import com.angrynerds.ai.pathfinding.AStarPathFinder;
import com.angrynerds.ai.pathfinding.ClosestHeuristic;
import com.angrynerds.ai.pathfinding.Path;
import com.angrynerds.game.World;
import com.angrynerds.game.screens.play.PlayScreen;
import com.angrynerds.gameobjects.creatures.Creature;
import com.angrynerds.gameobjects.creatures.Goblin;
import com.angrynerds.input.TouchInput;
import com.angrynerds.util.State;
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
    private PlayScreen playScreen;
    private Map map;
    private World world;
    private Player player;
    private float vX;
    private float vY;
    private float vX_MAX = 270;
    private float vY_MAX = 210;
    private float pX;
    private float pY;
    private Path path;
    private AStarPathFinder pathFinder;
    public float health = 100;



    private AStarPathFinder pf;

    private Random random;
    private Boolean bol = true;
    private Timer timer;
    private Vector2 velocity = new Vector2();
    private Animation ani;


    private int speed = 120;
    private int tolerance = 1;
    private int rea = 0;

    private boolean alive  = true;
    private boolean flipped = false;


//    public Enemy(PlayScreen playScreen,Player player){
//        super();
//        this.playScreen = playScreen;
//        map = screena.playController.world.map;
//        this.player = player;
//
//
//    }

    public Enemy(String name, String path, String skin, Player player, float scale) {
        super(name, path, skin, scale);

        this.player = player;
        x = 300;
        y = 150;
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

        // params



        ani = skeletonData.findAnimation("move");






    }


    public void render(SpriteBatch batch) {
        super.render(batch);



    }


    public void update(float deltatime) {
        super.update(deltatime);

        // Orientation to player


        //Creates new Path to Player
        path = pathFinder.findPath(1, (int) (x) / map.getTileWidth(), (int) (y) / map.getTileHeight(), (int) (player.x) / map.getTileWidth(), (int) (player.y) / map.getTileHeight());
        if (alive){
            skeleton.setFlipX((player.x - x >= 0));
        if (path != null && rea < path.getLength()) {

            if (!isReached(rea)) {

                Vector2 nextStep = new Vector2((float) path.getStep(1).getX() * map.getTileWidth(), (float) path.getStep(1).getY() * map.getTileHeight());
                float angle = (float) Math.atan2(path.getStep(rea).getY() * map.getTileHeight() - y, path.getStep(rea).getX() * map.getTileWidth() - x);
                velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);


                // velocity.set( (nextStep.x - getX()),(nextStep.y - getY()));

                if (x != nextStep.x) {
                    x = x + velocity.x * deltatime;

                }
                if (y != nextStep.y)
                    y = (y + velocity.y * deltatime);


            } else if (isReached(rea)) {

                rea++;
            }
            if (rea == path.getLength()) {
                System.out.println("New Path");

            }

            if(path.getLength() <= 3)
                ani = skeletonData.findAnimation("attack");
            else
                ani = skeletonData.findAnimation("move");

        }
            ani.apply(skeleton, skeleton.getTime(), skeleton.getTime(), true, null);
        }

        else
        ani.mix( skeleton, skeleton.getTime(), skeleton.getTime(), false, null,1);
       // ani.apply(skeleton, skeleton.getTime(), skeleton.getTime(), false, null);




       // if(getSkeletonBounds().aabbIntersectsSkeleton(player.getSkeletonBounds()))
       // System.out.println(getSkeletonBounds().getMaxX());





        // path = pf.findPath(1,(int)position.x/map.getTileWidth(),(int)position.y/map.getTileHeight(),(int)player.position.x/map.getTileWidth(),(int) player.position.y/map.getTileHeight());

        // for(int i= 0, len =  path.getLength(); i< len;i++){
        /**
         int nextDestinationX = path.getStep(i).getX()*map.getTileWidth();
         int nextDestinationY = path.getStep(i).getY()*map.getTileHeight();
         while( position.x != nextDestinationX || position.y != nextDestinationY){
         if(nextDestinationX<position.x)  {
         position.x -= 1;
         System.out.println("Walking" + i);                    }
         else if(nextDestinationX>position.x){
         position.x += 1;

         }
         else  if(nextDestinationY<position.y){
         position.y -= 1 ;

         }

         else   if(nextDestinationY > position.y){
         position.y +=1;

         }
         setPosition(position.x,position.y);
         }  */


    }




       /*
        if(position.x != pX && position.y != pY){
          if(pX + player.dimension.x  < position.x ){

            //System.out.println("Going Left");
            position.x -= 1;
          }
           if(pY + player.dimension.y < position.y)     {

             position.y -= 1;
            //System.out.println("Going Down");
           }

            if(pX  > position.x + this.dimension.x){

                //System.out.println("Going Ricght");
                position.x += 1;
            }
            if(pY  > position.y + this.dimension.y)     {

                position.y += 1;
                //  System.out.println("Going Up");
            }
        }
            */
    //  for(int i = 0; i<path.getLength();i++){

    // setPosition(path.getStep(i).getX(),path.getStep(i).getY());
    //    System.out.println("Walking");
    // }

    public void hit(int healthDecrease){
         if(player.getState() != State.ATTACKING){
        health -= healthDecrease;
        if(health <= 0)   {
           die();

        }
        }

    }
    public void die(){


        ani = skeletonData.findAnimation("die");
        alive = false;
    }

    private boolean isReached(int i) {

        return Math.abs(path.getStep(i).getX() * map.getTileWidth() - getX()) <= speed / tolerance * Gdx.graphics.getDeltaTime() &&
                Math.abs(path.getStep(i).getY() * map.getTileHeight() - getY()) <= speed / tolerance * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void attack() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
