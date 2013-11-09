package com.angrynerds.ai.pathfinding;

import com.angrynerds.gameobjects.Map;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SortedIntList;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian
 * Date: 07.11.13
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class AStarPathFinder {

    private final Map map;
    private Path path;
    private AStarHeuristic heuristic;
    private int maxSearchDistance;
    private boolean allowDiagMovement;
    private Node[][] nodes;
    private ArrayList closedList = new ArrayList();
    private SortedList openList = new SortedList();


    public AStarPathFinder(Map map, int maxSearchDistance,
                           boolean allowDiagMovement, AStarHeuristic heuristic) {
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;

        nodes = new Node[map.getNumTilesX()][map.getNumTilesY()];
        for (int x = 0; x < map.getNumTilesX(); x++) {
            for (int y = 0; y < map.getNumTilesY(); y++) {
                nodes[x][y] = new Node(x, y);
            }
        }

    }

    public Path findPath(int enemieType, int sx, int sy, int tx, int ty) {

      //  if (map.isSolid(tx, ty)) {
      //
      //      return null;

      //  }

        /** Init A Star */

        nodes[sx][sy].setDepth(0);
        nodes[sx][sy].setCost(0);
        closedList.clear();
        openList.clear();
        openList.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;


        int currentDepth = 0;
        Node z  = (Node) openList.getByIndex(0);
        int zX  = z.getX();
        int zY = z.getY();
        Array<Rectangle> bol = map.getCollisionObjects(zX, zY);

        while (currentDepth <= maxSearchDistance && openList.getSize() != 0) {

            Node currentNode = getFirstInOpen();
            if (currentNode == nodes[tx][ty])
                break;

            removeFromOpen(currentNode);
            addToClosed(currentNode);
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x != 0 && y != 0) {
                        int xp = x + currentNode.getX();
                       // int yp;

                         int   yp = y + currentNode.getY();



                        if (isValidLocation(1, sx, sy, xp, yp)) {

                            float nextStepCost = currentNode.getCost() + getMovementCost();

                            Node neighbour = nodes[xp][yp];
                            map.pathFinderVisited(xp, yp);

                            if (nextStepCost < neighbour.getCost()) {
                                if (inOpenList(neighbour)) {
                                    removeFromOpen(neighbour);
                                }
                                if (inClosedList(neighbour)) {
                                    removeFromClosed(neighbour);
                                }
                            }


                            if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                                neighbour.setCost(nextStepCost);
                                neighbour.setHeuristic(getHeuristicCost(1, xp, yp, tx, ty));
                                currentDepth = Math.max(currentDepth, neighbour.setParent(currentNode));
                                addToOpen(neighbour);

                            }


                        }


                    }


                }
            }

        }

        if(nodes[tx][ty].parent == null)
            return null;

        Path path = new Path();
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            path.prependStep(target.getX(), target.getY());
            target = target.parent;
        }
        path.prependStep(sx,sy);


        return path;
    }


    protected boolean isValidLocation(int mover, int sx, int sy, int x, int y) {
        boolean invalid = ((x < 0) || (y < 0) ) || (x >= map.getNumTilesX() || (y >= map.getNumTilesY()));

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = map.isSolid(x, y);

        }

        return !invalid;

        //return true;
    }


    private boolean inOpenList(Object o) {


        return openList.contains(o);
    }

    private float getHeuristicCost(int TYPE, int xp, int yp, int tx, int ty) {
        return heuristic.getCost(map, TYPE, xp, yp, tx, ty);
    }

    boolean inClosedList(Object o) {

        return closedList.contains(o);
    }

    private void removeFromClosed(Object o) {

        closedList.remove(o);
    }


    private Node getFirstInOpen() {
        return (Node) openList.getFirst();
    }

    private void removeFromOpen(Node node) {
        openList.remove(node);
    }

    private void addToClosed(Node node) {

        closedList.add(node);
    }

    private void addToOpen(Node node) {

        openList.add(node);
    }

    private int getMovementCost() {
       // return new Random().nextInt(5);
       return 1;
    }

}
