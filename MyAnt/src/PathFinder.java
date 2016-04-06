import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


public class PathFinder {

    protected Ants gameManager;
    private final int SEARCH_LIMIT = 1000;
    private final Set<Tile> myAnts4Combat = new HashSet<Tile>();
    private final Set<Tile> enemyAnts4Combat = new HashSet<Tile>();
    private final Set<Aim> direction2Escape = new HashSet<Aim>();

    public PathFinder() {

    }

    /*
     * Move ant to any kinds of adjacent tile we want
     */
    public void takeAStep(Tile ant, Tile target) {

        List<Aim> directions = gameManager.getDirections(ant, target);
        for (Aim direction : directions) {
            if (gameManager.getIlk(ant, direction).isPassable() && !gameManager.getNextTurnAnts().contains(target)) {
                gameManager.issueOrder(ant, direction);
                break;
            }
        }
    }

    public Tile getAnt4Target(Tile target) {
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = SEARCH_LIMIT;
        /*
         * BFS here!
		 */
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(target);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            tile = qe.remove();
            if (isMyAntTile(tile)) {    //found best ant for this food
                //DEBUG
                System.out.println("number of search = " + searchCount);
                return tile;
            }
            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (!visitedTile.contains(neighbor)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
            searchCount++;
        }
        //DEBUG
        System.out.println("number of search = " + searchCount);
        return null;    //can't find any ants for this food
    }

    public boolean isPassAbleTile(Tile tile, Aim direction) {
        return (gameManager.getIlk(tile, direction).isPassable()
                && gameManager.isNotNextTurnAnts(gameManager.getTile(tile, direction))
        );
    }

    public boolean isMyAntTile(Tile tile) {
        return gameManager.map[tile.getRow()][tile.getCol()].equals(Ilk.MY_ANT);
    }

    public boolean isEnemyAntTile(Tile tile) {
        return gameManager.map[tile.getRow()][tile.getCol()].equals(Ilk.ENEMY_ANT);
    }

    public boolean isVisibleTile(Tile tile) {
        return gameManager.isVisible(tile);
    }

    /*
     * Use BFS to move a closest ant from the target one step towards the target, then return it.
     */
    public Tile assignTarget2Ant(Tile target) {
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = SEARCH_LIMIT;
		// BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(target);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            //while(!qe.isEmpty() ){
            tile = qe.remove();

            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (isMyAntTile(neighbor)) {
                        takeAStep(neighbor, tile);
                        return neighbor;
                    }
                    if (!visitedTile.contains(neighbor)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
            searchCount++;
        }
        return null;
    }

    public Tile getPathFromAnt2Target(Tile myAnt, Tile target) {
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = SEARCH_LIMIT;
		// BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(target);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            tile = qe.remove();

            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (neighbor.compareTo(myAnt) == 0) {
                        return tile;
                    }

                    if (!visitedTile.contains(neighbor)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
            searchCount++;
        }
        return null;
    }

    public boolean assignAnt2Target(Tile myAnt, Tile target) {
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = SEARCH_LIMIT;
        // BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(target);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            tile = qe.remove();

            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (neighbor.compareTo(myAnt) == 0) {
                        //if( !gameManager.isNextTurnAnts(tile) ){
                        takeAStep(myAnt, tile);
                        return true;
                        //}
                    }

                    if (!visitedTile.contains(neighbor)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
            searchCount++;
        }
        return false;
    }

    public boolean assignAnt2TargetByLimit(Tile myAnt, Tile target, int limit) {
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = limit;
        // BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(target);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            tile = qe.remove();

            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (neighbor.compareTo(myAnt) == 0) {
                        takeAStep(myAnt, tile);
                        //gameManager.setAnt2TargetMap(tile, target);
                        return true;
                    }

                    if (!visitedTile.contains(neighbor)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
            searchCount++;
        }
        return false;
    }

    public boolean assignAnt2TargetNoLimit(Tile myAnt, Tile target) {
        Tile tile = null;

        // BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(target);

        while (!qe.isEmpty()) {
            tile = qe.remove();

            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (neighbor.compareTo(myAnt) == 0) {
                        takeAStep(myAnt, tile);
                        //gameManager.setAnt2TargetMap(tile, target);
                        return true;
                    }

                    if (!visitedTile.contains(neighbor)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
        }
        return false;
    }

    public Tile getPathByLastSeenTile(Tile myAnt, Tile lastSeen) {
        //BFS to find invisible tile
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = 1000;
        int maxLastSeenTileCount = 800;
        // BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(myAnt);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            tile = qe.remove();
            if (!isVisibleTile(tile)) {
                //if( isOnUnseenPath(tile, myAnt, lastSeen) )
                //assignAnt2Target(myAnt, tile);
                return tile;
            }
            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);

                    if (searchCount < maxLastSeenTileCount)
                        if (!visitedTile.contains(neighbor) && isOnUnseenPath(neighbor, myAnt, lastSeen)) {
                            qe.add(neighbor);
                            visitedTile.add(neighbor);
                        } else if (!visitedTile.contains(neighbor)) {
                            qe.add(neighbor);
                            visitedTile.add(neighbor);
                        }
                }
            }
            searchCount++;
        }
        return null;
    }

    public boolean exploreByLastSeenTile(Tile myAnt, Tile lastSeen) {
        //BFS to find invisible tile
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = 1000;
        int maxLastSeenTileCount = 800;
        // BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(myAnt);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            tile = qe.remove();
            if (!isVisibleTile(tile)) {
                //if( isOnUnseenPath(tile, myAnt, lastSeen) )
                assignAnt2Target(myAnt, tile);
                return true;
            }
            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);

                    if (searchCount < maxLastSeenTileCount)
                        if (!visitedTile.contains(neighbor) && isOnUnseenPath(neighbor, myAnt, lastSeen)) {
                            qe.add(neighbor);
                            visitedTile.add(neighbor);
                        } else if (!visitedTile.contains(neighbor)) {
                            qe.add(neighbor);
                            visitedTile.add(neighbor);
                        }
                }
            }
            searchCount++;
        }
        return false;
    }

    public boolean isOnUnseenPath(Tile unSeen, Tile myAnt, Tile lastSeen) {
        if (myAnt.getRow() == lastSeen.getRow()) {
            if (myAnt.getCol() < lastSeen.getCol() && unSeen.getCol() < lastSeen.getCol())
                return true;
            if (myAnt.getCol() > lastSeen.getCol() && unSeen.getCol() > lastSeen.getCol())
                return true;
        } else if (myAnt.getCol() == lastSeen.getCol()) {
            if (myAnt.getRow() < lastSeen.getRow() && unSeen.getRow() < lastSeen.getRow())
                return true;
            if (myAnt.getRow() > lastSeen.getRow() && unSeen.getRow() > lastSeen.getRow())
                return true;
        }

        return false;
    }

    public boolean followFriendByLastSeenTile(Tile myAnt, Tile lastSeen) {
        //BFS to find invisible tile
        Tile tile = null;
        int searchCount = 0;
        int searchDepth = SEARCH_LIMIT;
        // BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(myAnt);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            tile = qe.remove();
            if (isMyAntTile(tile)) {
                //if( isOnUnseenPath(tile, myAnt, lastSeen) )
                assignAnt2Target(myAnt, tile);
                return true;
            }
            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (!visitedTile.contains(neighbor) && isOnUnseenPath(neighbor, myAnt, lastSeen)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
            searchCount++;
        }
        return false;
    }

    public void combatOnEnemy(Tile enemyAnt) {
        myAnts4Combat.clear();
        enemyAnts4Combat.clear();

        Tile tile = null;
        int searchCount = 0;
        int searchDepth = 10;

        // BFS
        Queue<Tile> qe = new LinkedList<Tile>();
        HashSet<Tile> visitedTile = new HashSet<Tile>();
        qe.add(enemyAnt);

        while (!qe.isEmpty() && searchCount < searchDepth) {
            //while(!qe.isEmpty() ){
            tile = qe.remove();

            for (Aim direction : Aim.values()) {
                if (isPassAbleTile(tile, direction)) {
                    Tile neighbor = gameManager.getTile(tile, direction);
                    if (isMyAntTile(neighbor)) {
                        myAnts4Combat.add(neighbor);
                    }
                    if (isEnemyAntTile(neighbor)) {
                        enemyAnts4Combat.add(neighbor);
                    }
                    if (!visitedTile.contains(neighbor)) {
                        qe.add(neighbor);
                        visitedTile.add(neighbor);
                    }
                }
            }
            searchCount++;
        }

        if (myAnts4Combat.size() >= enemyAnts4Combat.size()) {
            for (Tile myAnt : myAnts4Combat)
                assignAnt2Target(myAnt, enemyAnt);
        } else {
            //escape
            for (Tile myAnt : myAnts4Combat)
                getAwayFromTile(myAnt, enemyAnt);
        }

    }

    public void getAwayFromTile(Tile myAnt, Tile enemyAnt) {
        direction2Escape.clear();

        if (myAnt.getRow() > enemyAnt.getRow())
            direction2Escape.add(Aim.SOUTH);
        if (myAnt.getRow() < enemyAnt.getRow())
            direction2Escape.add(Aim.NORTH);
        if (myAnt.getCol() > enemyAnt.getCol())
            direction2Escape.add(Aim.EAST);
        if (myAnt.getCol() < enemyAnt.getCol())
            direction2Escape.add(Aim.WEST);

        for (Aim direction : direction2Escape) {
            if (gameManager.getIlk(myAnt, direction).isPassable() &&
                    !gameManager.getNextTurnAnts().contains(gameManager.getIlk(myAnt, direction))
                    )
                gameManager.issueOrder(myAnt, direction);
        }
    }


}
