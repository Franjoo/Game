package com.angrynerds.ai.pathfinding;

import com.angrynerds.gameobjects.Map;
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
    private ArrayList closedList;
    private SortedList openList;



    public AStarPathFinder(Map map, int maxSearchDistance,
                           boolean allowDiagMovement, AStarHeuristic heuristic) {
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;

        nodes = new Node[map.getNumTilesX()][map.getNumTilesY()];
        for (int x=0;x<map.getNumTilesX();x++) {
            for (int y=0;y<map.getNumTilesY();y++) {
                nodes[x][y] = new Node(x,y);
            }
        }
    }
    public Path findPath(int enemieType, int sx,int sy,int tx, int ty){

        if(map.isSolid(tx,ty)){

            return null;

        }

        /** Init A Star */
        nodes[sx][sy].setDepth(0);
        nodes[sx][sy].setCost(0);
        closedList.clear();
        openList.clear();
        openList.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;


        int currentDepth  =  0;

        while(currentDepth <= maxSearchDistance && openList.getSize() != 0){

            Node currentNode = getFirstInOpen();
            if(currentNode == nodes[tx][ty])
                break;

            removeFromOpen(currentNode);
            addToClosed(currentNode);
            for (int x=-1;x<=1;x++) {
                for (int y=-1;y<=1;y++) {
                    if(x!= 0 && y != 0){
                        int xp = x + currentNode.getX();
                        int yp = y + currentNode.getY();

                        int nextStepCost = currentNode.getCost() + getMovementCost();
                        Node neighbour = nodes[xp][yp];

                         if(nextStepCost < neighbour.getCost()) {
                             if (inOpenList(neighbour)) {
                                 removeFromOpen(neighbour);
                             }
                             if (inClosedList(neighbour)) {
                                 removeFromClosed(neighbour);
                             }
                         }

                         }









                    }




        }
            }




    return path;
    }

    protected boolean isValidLocation(int mover, int sx, int sy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.getNumTilesX()) || (y >= map.getNumTilesY());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = map.isSolid(x, y);
        }

        return !invalid;
    }


    private boolean inOpenList(Object o){


        return openList.contains(o);
    }

    private boolean inClosedList(Object o){

        return closedList.contains(o);
    }

    private void removeFromClosed(Object o){

        closedList.remove(o);
    }



    private Node getFirstInOpen() {
        return (Node) openList.getFirst();
    }
    private void removeFromOpen(Node node) {
        openList.remove(node);
    }

   private void addToClosed(Node node){

      closedList.add(node);
   }
            @Override
   private int getMovementCost(){
          return new Random().nextInt(5);

   }
}
