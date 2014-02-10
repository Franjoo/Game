package com.angrynerds.gameobjects;

import com.angrynerds.ai.pathfinding.AStarPathFinder;
import com.angrynerds.ai.pathfinding.Path;
import com.angrynerds.gameobjects.creatures.Creature;
import com.angrynerds.gameobjects.map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;

import java.util.Random;

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
    private int attackCounter = 0;



    private Path path;
    private AStarPathFinder pathFinder;
    private int nextStepInPath = 1;

    private Random gen = new Random();

    public int health;
    public int type;

    Vector2 nextStep = new Vector2();
    float angle;


    private Vector2 velocity = new Vector2();
    private int speed = 120;
    private float tolerance = 1.0f;
    private boolean alive = true;

    private Animation ani;

    private int xTilePosition;
    private int yTilePosition;
    private int xTilePlayer;
    private int yTilePlayer;

    private AnimationState state;
    private AnimationListener animationListener;

    private int atckDmg;


    public Enemy(String name, String path, String skin, Player player, float scale) {
        super(name, path, skin, scale);

        this.player = player;
        this.health = 100;
        this.atckDmg = 5;
        this.type = 1;
    }


    public Enemy(String name, String path, String skin, Player player, float scale, int health, int atckDmg, int type) {
        super(name, path, skin, scale);

        this.player = player;
        this.health = health;
        this.atckDmg = atckDmg;
        this.type = type;
    }

    public Enemy(float x, float y, String name, String path, String skin, Player player, float scale) {
        super(name, path, skin, scale);
        this.x = x;
        this.y = y;

        this.player = player;

        init();
    }

    private void setAnimationStates() {

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
        stateData.setMix("move", "attack", 0.6f);
        stateData.setMix("attack", "move", 0.6f);
        stateData.setMix("attack", "die", 0.5f);
        stateData.setMix("move", "die", 0.2f);


        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        animationListener = new AnimationListener();
        state.addListener(animationListener);
        state.setAnimation(0, "move", true);
    }


    public void init() {

        map = Map.getInstance();
        pathFinder = AStarPathFinder.getInstance();

        ani = skeletonData.findAnimation("move");
        updatePositions();
        path = getNewPath();
        ranX = -1 + (int) (+(Math.random() * 3));
        ranY = -1 + (int) (+(Math.random() * 3));
        setAnimationStates();

    }

    public void init(float x, float y) {

        this.x = x;
        this.y = y;

        map = Map.getInstance();
        pathFinder = AStarPathFinder.getInstance();

        ani = skeletonData.findAnimation("move");
        updatePositions();
        path = getNewPath();
        ranX = -1 + (int) (+(Math.random() * 3));
        ranY = -1 + (int) (+(Math.random() * 3));


    }


    public void render(SpriteBatch batch) {
        super.render(batch);


    }


    public void update(float deltatime) {
        super.update(deltatime);
        path = getNewPath();
            if (alive) {

                updatePositions();

                if (path != null && path.getLength() >= 2)  {
                    moveToPlayer(deltatime);
                    ani.apply(skeleton, skeleton.getTime(), skeleton.getTime(), true, null);
                }
                else {
                    ani = skeletonData.findAnimation("attack");
                    ani.apply(skeleton, skeleton.getTime(), skeleton.getTime(), true, null);
                    if(attackCounter == 180){
                        attack();
                        attackCounter = 0;
                    }
                    else
                        attackCounter++;


                }

            }
        else{

        ani = skeletonData.findAnimation("die");
        ani.apply(skeleton, skeleton.getTime(), skeleton.getTime(), false, null);
            }
    }

    public void updatePositions() {

        xTilePosition = (int) (x) / map.getTileWidth();
        yTilePosition = (int) (y) / map.getTileHeight();
        xTilePlayer = (int) (player.x) / map.getTileWidth();
        yTilePlayer = (int) (player.y) / map.getTileHeight();
    }


    public Path getNewPath() {
       // updatePositions();
        return pathFinder.findPath(1, xTilePosition, yTilePosition, xTilePlayer + ranX, yTilePlayer + ranY);
    }

    public int getTilePostionX() {

        return xTilePosition;
    }

    public int getTilePostionY() {

        return yTilePosition;
    }


    public void moveToPlayer(float deltatime) {



            skeleton.setFlipX((player.x - x >= 0));

            if (path != null && nextStepInPath < path.getLength()) {

                nextStep = new Vector2((float) path.getStep(nextStepInPath).getX() * map.getTileWidth(), (float) path.getStep(nextStepInPath).getY() * map.getTileHeight());
                angle = (float) Math.atan2(path.getStep(nextStepInPath).getY() * map.getTileHeight() - y, path.getStep(nextStepInPath).getX() * map.getTileWidth() - x);
                velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);


                if ((int) x != nextStep.x) {
                    x = x + velocity.x * deltatime;

                }

                if (yTilePosition != nextStep.y) {
                    y = (y + velocity.y * deltatime);
                }


//         if(isReached(nextStepInPath)){
//                System.out.println("Reached");
//                nextStepInPath++;
//
//
//            }


//            if (nextStepInPath == path.getLength()-1) {
//                System.out.println("New Path");
//                path = getNewPath();
//                nextStepInPath = 1;
//
//
//            }

                ani = skeletonData.findAnimation("move");
            }
        }




    public void die() {
        state.addAnimation(0, "die", false, 0);
        alive = false;

    }

    private boolean isReached(int i) {


        return Math.abs(path.getStep(i).getX() * map.getTileWidth() - getX()) <= speed / tolerance * Gdx.graphics.getDeltaTime() &&
                Math.abs(path.getStep(i).getY() * map.getTileHeight() - getY()) <= speed / tolerance * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void attack() {

            if (player.getSkeletonBounds().aabbIntersectsSkeleton(getSkeletonBounds()))
                player.setActualHP(player.getActualHP() - atckDmg);

    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health <= 0) alive = false;
    }

    class AnimationListener implements AnimationState.AnimationStateListener {

        @Override
        public void event(int trackIndex, Event event) {
//            System.out.println(trackIndex + " event: " + state.getCurrent(trackIndex) + ", " + event.getData().getName());
        }

        @Override
        public void complete(int trackIndex, int loopCount) {
//            System.out.println(trackIndex + " complete: " + state.getCurrent(trackIndex) + ", " + loopCount);
//            System.out.println(state.getCurrent(trackIndex));
            if (state.getCurrent(trackIndex).toString().equals("jump")) {
                state.setAnimation(0, "run_test", true);
            }
        }

        @Override
        public void start(int trackIndex) {
//            System.out.println(trackIndex + " start: " + state.getCurrent(trackIndex));
        }

        @Override
        public void end(int trackIndex) {
//            System.out.println(trackIndex + " end: " + state.getCurrent(trackIndex));
        }
    }

}
