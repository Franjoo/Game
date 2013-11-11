package com.angrynerds.gameobjects;

import com.angrynerds.ai.pathfinding.AStarPathFinder;
import com.angrynerds.ai.pathfinding.ClosestHeuristic;
import com.angrynerds.ai.pathfinding.Path;
import com.angrynerds.game.World;
import com.angrynerds.game.screens.PlayScreen;
import com.angrynerds.input.TouchInput;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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
    private Path path2;

    private AStarPathFinder pf;

    private Random random;
    private Boolean bol = true;
    private Timer timer;
    private Vector2 velocity = new Vector2();
    private Texture showPath;
    private int speed = 50;
    private int tolerance = 1;
    int rea = 0;



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
        path2 = pf.findPath(1,(int) (position.x / map.getTileWidth()),(int) (position.y / map.getTileHeight()),(int )(player.position.x / map.getTileWidth()), (int)(player.position.y / map.getTileHeight()));
       // path = pf.findPath(1,3,3,19,5) ;
        System.out.println("Starttile: " + position.x / map.getTileWidth() + "     " + position.y / map.getTileHeight() + "EndTile:    " +  player.position.x / map.getTileWidth() + "     " +    player.position.y / map.getTileHeight());
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
         getPathLine();
        setTexture(t);


    }

    public Texture getPathLine(){
        Pixmap p = new Pixmap(map.getMapWidth(), map.getMapHeight(), Pixmap.Format.RGBA8888);

        Color color_outline = new Color(1, 0, 0, 1);
        p.setColor(color_outline);

        for(int i = 1; i<path.getLength();i++){
            int x0 = path.getStep(i-1).getX()*map.getTileWidth();// + map.getTileWidth()/2;
            int x1 = path.getStep(i). getX()*map.getTileWidth();// + map.getTileWidth()/2;
            int y0 = path.getStep(i-1).getY()*map.getTileHeight();// + map.getTileHeight()/2;
            int y1 = path.getStep(i). getY()*map.getTileHeight();// + map.getTileHeight()/2;
            p.drawLine(x0, y0, x1, y1);
           // System.out.println(x0+"    "+x1+"    "+y0+"    "+y1);
        }


      //  p.drawLine(0,0,100,100);
        showPath = new Texture(p);



           return showPath;
    }

    public void render(SpriteBatch batch){
        update(Gdx.graphics.getDeltaTime());
        batch.begin();



        draw(batch);batch.draw(showPath, 0, 0);
        batch.end();


    }

    public void newPath(int x, int y){
        pX = player.position.x;
        pY = player.position.y;
        path = pf.findPath(1,x/map.getTileWidth(),y/map.getTileWidth(),(int)pX/map.getTileWidth(),(int)pY/map.getTileHeight());
    }

    public void update(float deltatime){


        /*
        if(path != null && path.getStep(0) != null)   {
            setPosition(path.getStep(0).getX(),path.getStep(0).getY());
           System.out.println(position.x + "     " +position.y);
               path.removeStep(0);

        }
         */

       if( pf.findPath(1,(int)(position.x+map.getTileWidth()/2)/map.getTileWidth(),(int)(position.y+ map.getTileHeight()/2)/map.getTileHeight() ,(int)(player.position.x+ map.getTileWidth()/2)/map.getTileWidth() ,(int) (player.position.y+ map.getTileHeight()/2)/map.getTileHeight() )!= null)
        path = pf.findPath(1,(int)(position.x+map.getTileWidth()/2)/map.getTileWidth(),(int)(position.y+ map.getTileHeight()/2)/map.getTileHeight() ,(int)(player.position.x+ map.getTileWidth()/2)/map.getTileWidth() ,(int) (player.position.y+ map.getTileHeight()/2)/map.getTileHeight() );
        if(path != null && rea<path.getLength() ){


            if (!isReached(rea)) {

                Vector2 nextStep =new Vector2((float) path.getStep(1).getX()*map.getTileWidth() ,(float)path.getStep(1).getY()*map.getTileHeight());
                float angle = (float) Math.atan2(path.getStep(rea).getY()*map.getTileHeight() - getY(), path.getStep(rea).getX()*map.getTileWidth() - getX());
                velocity.set( (float) Math.cos( angle) * speed,(float) Math.sin(  angle)*speed);


              // velocity.set( (nextStep.x - getX()),(nextStep.y - getY()));

              if(position.x != nextStep.x)
                 position.x = position.x +  velocity.x  * deltatime;
                if(position.y != nextStep.y)
                 position.y =  position.y +  velocity.y  * deltatime  ;

                setPosition(position.x, position.y);
                   }
            else if(isReached(rea)){
                System.out.println(rea);
                System.out.println(path.getLength());
                rea++;
            }
            if(rea == path.getLength()){
                System.out.println("New Path");

            }
        }
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


    private boolean isReached(int i) {

        return Math.abs(path.getStep(i).getX()*map.getTileWidth() - getX()) <= speed / tolerance * Gdx.graphics.getDeltaTime() &&
        Math.abs(path.getStep(i).getY()*map.getTileHeight() - getY()) <=speed / tolerance * Gdx.graphics.getDeltaTime();
    }

}
