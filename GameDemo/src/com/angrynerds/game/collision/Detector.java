package com.angrynerds.game.collision;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

/**
 * User: Franjo
 * Date: 11.11.13
 * Time: 15:15
 * Project: TileRunner
 */
public class Detector {
    private static String TAG = Detector.class.getSimpleName();

    private static Detector instance = null;

    // tiled map properties
    public TiledMap tiledMap;
    private int numTilesX;
    private int numTilesY;
    private int tileWidth;
    private int tileHeight;
    private int mapWidth;
    private int mapHeight;

    // map lists
    private Array<TiledMapTileLayer> collisionTileLayers;
    private boolean[][] solids;

    private int yMax;

//    private Array<Rectangle> collisionObjects;
//    private Array<TmxMapObject> mapObjects;
//    private HashMap<String, TextureRegion> regionMap;


    private Detector(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        init();

        instance = this;
    }

    private void init() {

        // parse map properties
        numTilesX = Integer.parseInt(tiledMap.getProperties().get("width").toString());
        numTilesY = Integer.parseInt(tiledMap.getProperties().get("height").toString());
        tileWidth = Integer.parseInt(tiledMap.getProperties().get("tilewidth").toString());
        tileHeight = Integer.parseInt(tiledMap.getProperties().get("tileheight").toString());
        mapWidth = numTilesX * tileWidth;
        mapHeight = numTilesY * tileHeight;

        collisionTileLayers = getCollisionTileLayers();

        // fill boolean solid array
        solids = new boolean[numTilesX][numTilesY];
        for (int w = 0; w < solids.length; w++) {
            for (int h = 0; h < solids[w].length; h++) {
                for (int i = 0; i < collisionTileLayers.size; i++) {
                    TiledMapTileLayer.Cell cell = collisionTileLayers.get(i).getCell(w, h);
                    solids[w][h] = cell != null;
                }
            }
        }

        if (tiledMap.getProperties().containsKey("walkable")) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            yMax = Integer.parseInt(tiledMap.getProperties().get("walkable").toString()) * tileHeight;
            System.out.println("ymax: " + yMax);
        }

    }


    /**
     * checks whether there is a solid tile at specified position
     *
     * @param x position x
     * @param y position y
     * @return whether point collides with solid tile or not
     */
//    public boolean isSolid(final float x, final float y) {
//
//        for (int i = 0; i < collisionTileLayers.size; i++) {
//            TiledMapTileLayer.Cell cell = collisionTileLayers.get(i).getCell((int) (x) / tileWidth, (int) (y) / tileHeight);
//            if (cell != null) {
//                return true;
//            }
//        }
//        return false;
    public boolean isSolid(final float x, final float y) {
        if(y > yMax * 0.7f) return true;
        return solids[(int) (x) / tileWidth][(int) (y) / tileHeight];
    }

    /**
     * returns a collision tiled map tile layer that contains tiles which
     * represents collision objects.<br>
     * <p/>
     * note: the name of a collision tiled map tile layer starts with $c and must not contain a tmx object
     */
    private Array<TiledMapTileLayer> getCollisionTileLayers() {
        Array<TiledMapTileLayer> ctl = new Array<TiledMapTileLayer>();

        MapLayers layers = tiledMap.getLayers();
        for (int i = 0; i < layers.getCount(); i++) {
            if (layers.get(i).getName().startsWith("$c") && layers.get(i).getObjects().getCount() == 0) {
                ctl.add((TiledMapTileLayer) layers.get(i));
            }
        }
        return ctl;
    }


    //*** SINGLETON ***//
    public static Detector getInstance() {
        if (instance == null) throw new InstantiationError(TAG + " has not been initialized");
        return instance;
    }

    public static void initialize(TiledMap tiledMap) {
        new Detector(tiledMap);
    }

}
