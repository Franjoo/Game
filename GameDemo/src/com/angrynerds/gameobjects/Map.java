package com.angrynerds.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 20:30
 * Project: Main
 */
public class Map extends GameObject {

    // debug controlls
    private static final boolean SHOW_GRID = false;
    private static final boolean SHOW_COLLISION_SHAPES = true;
    private Texture gridTexture;
    private Texture collisionShapeTexture;

    private BitmapFont font;

    //    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    // map properties
    private int numTilesX;
    private int numTilesY;

    private int tileWidth;
    private int tileHeight;
    private int mapWidth;
    private int mapHeight;

    private Array<TiledMapTileLayer> collsionLayers;
    private Array<Rectangle> collisionObjects;

    private String mapPath = "data/maps/map_000.tmx";

    // global helper variables
    private Array<Rectangle> qArray = new Array<Rectangle>();

    public Map(OrthographicCamera camera) {
        this.camera = camera;

        init();
    }

    private void init() {
        map = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);

        // parse map properties
        int qWidth = Integer.parseInt(map.getProperties().get("width").toString());
        int qHeight = Integer.parseInt(map.getProperties().get("height").toString());
        int qTileWidth = Integer.parseInt(map.getProperties().get("tilewidth").toString());
        int qTileHeight = Integer.parseInt(map.getProperties().get("tileheight").toString());

        numTilesX = qWidth;
        numTilesY = qHeight;
        tileWidth = qTileWidth;
        tileHeight = qTileHeight;
        mapWidth = numTilesX * tileWidth;
        mapHeight = numTilesY * tileHeight;

        if (SHOW_GRID) {
            Pixmap pixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);
            pixmap.setColor(0, 0, 0, 1);
            for (int h = 0; h < mapHeight; h++) {
                for (int w = 0; w < mapWidth; w++) {
                    pixmap.drawRectangle(w * tileWidth, h * tileHeight, tileWidth, tileHeight);
                }
            }

            gridTexture = new Texture(pixmap);
//            font = new BitmapFont(Gdx.files.internal("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.internal("com/badlogic/gdx/utils/arial-15.png"), false);
//            font.setColor(0, 0, 0, 1);
//            font.scale(0.2f);
        }

        // fill collision relevant lists
        MapLayers layers = map.getLayers();

        collsionLayers = new Array<TiledMapTileLayer>();
        collisionObjects = new Array<Rectangle>();
        for (int i = 0; i < layers.getCount(); i++) {
            if (layers.get(i).getName().startsWith("$c.")) {
                if (layers.get(i).getObjects().getCount() == 0) {
                    collsionLayers.add((TiledMapTileLayer) layers.get(i));
                } else {

                    Array<HashMap<String, String>> objects = getObjectGroups(layers.get(i).getName());

                    for (MapObject object : map.getLayers().get("$c.CollisionObjectLayer").getObjects()) {

                             if(object instanceof  RectangleMapObject) {
                                 Rectangle rect = ((RectangleMapObject) object).getRectangle();
                                 collisionObjects.add(rect);
                                 System.out.println("Added");
                             }

                    }
                }
            }


        }

        if (SHOW_COLLISION_SHAPES) {
            Pixmap p = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);
            Color color_outline = new Color(0,0,0,1);
            Color color_fill = new Color(1,0,0,0.3f);
            for (int i = 0; i < collisionObjects.size; i++) {
                Rectangle r = collisionObjects.get(i);
                p.setColor(color_outline);
                p.drawRectangle((int) (r.getX()), (int) (r.getY()), (int) (r.getWidth()), (int) (r.getHeight()));
                p.setColor(color_fill);
                p.fillRectangle((int) (r.getX()), (int) (r.getY()), (int) (r.getWidth()), (int) (r.getHeight()));

            }
            for (int j = 0; j < collsionLayers.size; j++) {
                for (int h = 0; h < numTilesY; h++) {
                    for (int w = 0; w < numTilesX; w++) {
                        TiledMapTileLayer.Cell cell = collsionLayers.get(j).getCell(w, numTilesY - h - 1);
                        if (cell != null) {
                            p.setColor(color_outline);
                            p.drawRectangle(w * tileWidth, h * tileHeight, tileWidth, tileHeight);
                            p.setColor(color_fill);
                            p.fillRectangle(w * tileWidth, h * tileHeight, tileWidth, tileHeight);
                        }

                    }
                }
            }

            collisionShapeTexture = new Texture(p);

        }


//        TiledMapTileLayer l = (TiledMapTileLayer) layers.get(0);
//        System.out.println("--->" + l.getWidth());
//
//        for (int i = 0; i < layers.getCount(); i++) {
////            System.out.println(layers.get(i).getName());
//            if (layers.get(i).getName().startsWith("$c.")) {
//
//                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layers.get(i);
//
//                for (int k = 0; k < tileLayer.; k++) {
//
//
//                }
//
//                System.out.println("Collision Layer: " + layers.get(i).getName());
//
////                System.out.println(layers.get(0).getProperties().containsKey("1");
//
//                MapObjects objects = layers.get(i).getObjects();
////                map.getTileSets().
////                System.out.println("num objects: " + objects.getCount());
//                for (int j = 0; j < objects.getCount(); j++) {
//                    System.out.println(objects.get(j).hashCode());
//                }
//            }
//        }

    }

    private Array<HashMap<String, String>> getObjectGroups(final String layername) {

        Array<HashMap<String, String>> objects = new Array<HashMap<String, String>>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapPath));
            String line = reader.readLine();
            while (line != null) {
                if (line.trim().startsWith("<objectgroup") && line.contains("name=\"" + layername + "\"")) {
                    line = reader.readLine();
                    while (line.trim().startsWith("<object")) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        objects.add(hm);
                        String[] properties = line.trim().split(" ");
                        for (int i = 1; i < properties.length; i++) {
                            String k = properties[i].split("=")[0];
                            String v = properties[i].split("=")[1].replace('"', ' ').trim().replace("/>", "");

                            hm.put(k, v);
                        }

                        line = reader.readLine();
                    }
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objects;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.disableBlending();
        renderer.render();
        batch.enableBlending();

        if (SHOW_GRID) {
            batch.begin();
            batch.draw(gridTexture, 0, 0);

//            for (int h = 0; h < mapHeight; h++) {
//                for (int w = 0; w < mapWidth; w++) {
//                    font.draw(batch, "" + (h * mapWidth + w), w * tileWidth, h * tileHeight);
//                }
//            }
            batch.end();
        }

        if (SHOW_COLLISION_SHAPES) {
            batch.begin();
            batch.draw(collisionShapeTexture, 0, 0);
            batch.end();
        }


    }

    @Override
    public void update(float deltaTime) {
        renderer.setView(camera);
    }

    public boolean isSolid(float x, float y) {

        for (int i = 0; i < collsionLayers.size; i++) {
            TiledMapTileLayer.Cell cell = collsionLayers.get(i).getCell((int) (x) / tileWidth, (int) (y) / tileHeight);
            if (cell != null) {
                return true;
            }
        }
        return false;
    }

    public Rectangle getCollisionObjects(float x, float y) {

        for (int i = 0; i < collisionObjects.size; i++) {
            if (collisionObjects.get(i).contains(x, y)) {

               return collisionObjects.get(i);
            }
        }
        return null;
    }


    //<editor-fold desc="map property getters">
    public int getMapHeight() {
        return mapHeight;
    }

    public int getNumTilesX() {
        return numTilesX;
    }

    public int getNumTilesY() {
        return numTilesY;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }
    //</editor-fold>
}
