package com.angrynerds.gameobjects;

import com.angrynerds.ai.pathfinding.AStarPathFinder;
import com.angrynerds.ai.pathfinding.ClosestHeuristic;
import com.angrynerds.ai.pathfinding.Path;
import com.angrynerds.game.World;
import com.angrynerds.game.screens.PlayScreen;
import com.angrynerds.input.TouchInput;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * User: Basti
 * Date: 02.11.13
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
public class Enemie extends GameObject {
    private PlayScreen playScreen;
    private Map map;
    private World world;
    private Player player;
    private float vX;
    private float vY;
    private float vX_MAX = 3.2f * 60;
    private float vY_MAX = 3.2f * 60;
    private float pX;
    private float pY;
    private Path path;
    private AStarPathFinder pf;

    private Random random;
    private Boolean bol = true;
    private Timer timer;



    public Enemie(PlayScreen playScreen,Player player){
        super();
        this.playScreen = playScreen;
        map = playScreen.playController.world.map;
        this.player = player;


    }

    public Enemie(World world, Player player){
        this.world = world;

        this.player = player;

    }

    public void init(){
        map = Map.getInstance();
        this.timer = new Timer();



        position.x = 300;
        position.y = 150;
        dimension.x = 15;
        dimension.y = 15;
        setOrigin(0,0);
        setPosition(position.x,position.y);
        setSize(dimension.x,dimension.y);
        pf= new AStarPathFinder(map,500,true,new ClosestHeuristic());
        path = pf.findPath(1,(int) (position.x / map.getTileWidth()),(int) (position.y / map.getTileHeight()),(int )(player.position.x / map.getTileWidth()), (int)(player.position.y / map.getTileHeight()));

        random = new Random();


        System.out.println("bounds enemie: " + getBoundingRectangle().toString());

        Pixmap p = new Pixmap((int) (dimension.x), (int) (dimension.y), Pixmap.Format.RGBA8888);
        Texture t = new Texture(p.getWidth(), p.getHeight(), Pixmap.Format.RGBA8888);

        p.setColor(1, 0, 0, 1);
        p.fillRectangle(0, 0, (int) getWidth(), (int) getHeight());
        p.setColor(1, 0, 0, 1);
        p.fillRectangle((int) origin.x, (int) origin.y, 5, 5);
        p.setColor(1, 0, 0, 1);
        p.drawLine((int) getX(), (int) getY(), (int) (getWidth() + 20), (int) getY());
        p.drawLine((int) getX(), (int) getY(), (int) (getX()), (int) getHeight() + 20);

        t.draw(p, 0, 0);

        setTexture(t);


    }

    public void render(SpriteBatch batch){
        batch.begin();
        batch.draw(getTexture(), getX(), getY());
        for(int i = 0; i< path.getLength();i++)
            batch.draw(getTexture(),path.getStep(i).getX()*map.getTileWidth(), path.getStep(i).getY()*map.getTileHeight());
      draw(batch);
        batch.end();


    }

    public void update(float deltatime){
      pX = player.position.x;
      pY = player.position.y;
        /*
        if(path != null && path.getStep(0) != null)   {
            setPosition(path.getStep(0).getX(),path.getStep(0).getY());
           System.out.println(position.x + "     " +position.y);
               path.removeStep(0);

        }
         */
       // path = pf.findPath(1,(int)position.x/map.getTileWidth(),(int)position.y/map.getTileWidth(),(int)player.position.x/map.getTileWidth(),(int)player.position.y/map.getTileHeight());

        if(path != null ){

            // for(int i= 0, len =  path.getLength(); i< len;i++){
                 if(position.x != path.getStep(0).getX()*map.getTileWidth())    {
                     if(position.x < path.getStep(0).getX()*map.getTileWidth())
                     position.x += 1;
                    else if(position.x > path.getStep(0).getX()*map.getTileWidth())
                         position.x -= 1;

                 }

                 if(position.y != path.getStep(0).getY()*map.getTileHeight())  {
                     if(position.y < path.getStep(0).getY()*map.getTileHeight())
                         position.y += 1;
                     else if(position.y > path.getStep(0).getY()*map.getTileHeight())
                         position.y -= 1;
                 }

                setPosition(position.x,position.y);
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
    }

}
