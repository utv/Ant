import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


public class PathFinder {

	protected Ants gameManager;
	
	
	public PathFinder(){
		
	}
	
	/*
	 * Move ant to any kinds of adjacent tile we want
	 */
	private void takeAStep(Tile ant, Tile target){
		
		List<Aim> directions = gameManager.getDirections(ant, target);
		for(Aim direction : directions){
			if(gameManager.getIlk(ant, direction).isPassable()){
				gameManager.issueOrder(ant, direction);
				
				break;
			}
		}
	}

	public Tile getAnt4Target(Tile target) {
		Tile tile = null;
		int searchCount = 0;
		int searchDepth = 500;
		/*
		 * BFS here!
		 */
		Queue<Tile> qe = new LinkedList<Tile>();
		HashSet<Tile> visitedTile = new HashSet<Tile>();
		qe.add(target);
		
		while(!qe.isEmpty()&& searchCount < searchDepth ){		
			tile = qe.remove();
			if(isMyAntTile(tile)){	//found best ant for this food
				//DEBUG
				System.out.println("number of search = " + searchCount);
				return tile;
			}
			for(Aim direction : Aim.values()){
				if(isPassAbleTile(tile, direction)){
					Tile neighbor = gameManager.getTile(tile, direction);
					if(!visitedTile.contains(neighbor)){
						qe.add(neighbor);
						visitedTile.add(neighbor);
					}
				}
			}
			searchCount++;
		}
		//DEBUG
		System.out.println("number of search = " + searchCount);
		return null;	//can't find any ants for this food
	}
	
	private boolean isPassAbleTile(Tile tile, Aim direction) {
		return gameManager.getIlk(tile, direction).isPassable();
	}

	private boolean isMyAntTile(Tile tile) {
		return gameManager.map[tile.getRow()][tile.getCol()].equals(Ilk.MY_ANT);
	}
	
	private boolean isVisibleTile(Tile tile){
		return gameManager.isVisible(tile);
	}
	
	/*
	 * Use BFS to move a closest ant from the target one step towards the target, then return it.
	 */
	public Tile assignAnt2Target(Tile target){

		/*old version
		 * 	Tile ant = getAnt4Target(target);
			takeAStep(ant, target);
		*/
		
		Tile tile = null;
		int searchCount = 0;
		int searchDepth = 500;
		/*
		 * BFS here!
		 */
		Queue<Tile> qe = new LinkedList<Tile>();
		HashSet<Tile> visitedTile = new HashSet<Tile>();
		qe.add(target);
		
		while(!qe.isEmpty()&& searchCount < searchDepth ){		
		//while(!qe.isEmpty() ){	
			tile = qe.remove();
			
			for(Aim direction : Aim.values()){
				if(isPassAbleTile(tile, direction)){
					Tile neighbor = gameManager.getTile(tile, direction);
					if(isMyAntTile(neighbor)){
						takeAStep(neighbor, tile);
						return neighbor;
					}
					if(!visitedTile.contains(neighbor)){
						qe.add(neighbor);
						visitedTile.add(neighbor);
					}
				}
			}
			searchCount++;
		}
		return null;
	}
	
	public boolean assignAnt2Target(Tile myAnt, Tile target){
		Tile tile = null;
		int searchCount = 0;
		int searchDepth = 500;
		/*
		 * BFS here!
		 */
		Queue<Tile> qe = new LinkedList<Tile>();
		HashSet<Tile> visitedTile = new HashSet<Tile>();
		qe.add(target);
		
		while(!qe.isEmpty()&& searchCount < searchDepth ){		
			tile = qe.remove();
			
			for(Aim direction : Aim.values()){
				if(isPassAbleTile(tile, direction)){
					Tile neighbor = gameManager.getTile(tile, direction);
					if( neighbor.compareTo(myAnt) == 0 ){
						takeAStep(myAnt, tile);
						//gameManager.setAnt2TargetMap(tile, target);
						return true;
					}
						
					if(!visitedTile.contains(neighbor)){
						qe.add(neighbor);
						visitedTile.add(neighbor);
					}
				}
			}
			searchCount++;
		}
		return false;
	}
	
	public boolean assignAnt2TargetNoLimit(Tile myAnt, Tile target){
		Tile tile = null;
		/*int searchCount = 0;
		int searchDepth = 500;*/
		/*
		 * BFS here!
		 */
		Queue<Tile> qe = new LinkedList<Tile>();
		HashSet<Tile> visitedTile = new HashSet<Tile>();
		qe.add(target);
		
		while(!qe.isEmpty() ){		
			tile = qe.remove();
			
			for(Aim direction : Aim.values()){
				if(isPassAbleTile(tile, direction)){
					Tile neighbor = gameManager.getTile(tile, direction);
					if( neighbor.compareTo(myAnt) == 0 ){
						takeAStep(myAnt, tile);
						//gameManager.setAnt2TargetMap(tile, target);
						return true;
					}
						
					if(!visitedTile.contains(neighbor)){
						qe.add(neighbor);
						visitedTile.add(neighbor);
					}
				}
			}
		}
		return false;
	}
	
	

	public boolean exploreByLastSeenTile(Tile myAnt, Tile lastSeen) {
		//BFS to find invisible tile
		Tile tile = null;
		int searchCount = 0;
		int searchDepth = 500;
		/*
		 * BFS here!
		 */
		Queue<Tile> qe = new LinkedList<Tile>();
		HashSet<Tile> visitedTile = new HashSet<Tile>();
		qe.add(myAnt);
		
		while(!qe.isEmpty()&& searchCount < searchDepth ){		
			tile = qe.remove();
			if( ! isVisibleTile(tile) ){
				//if( isOnUnseenPath(tile, myAnt, lastSeen) )
					assignAnt2Target(myAnt, tile);
					return true;
			}
			for(Aim direction : Aim.values()){
				if(isPassAbleTile(tile, direction)){
					Tile neighbor = gameManager.getTile(tile, direction);
					if( !visitedTile.contains(neighbor) && isOnUnseenPath(neighbor, myAnt, lastSeen)){
						qe.add(neighbor);
						visitedTile.add(neighbor);
					}
				}
			}
			searchCount++;
		}
		return false;
	}

	private boolean isOnUnseenPath(Tile unSeen, Tile myAnt, Tile lastSeen) {
		if( myAnt.getRow() == lastSeen.getRow() ){
			if( myAnt.getCol() < lastSeen.getCol() && unSeen.getCol() < lastSeen.getCol() )
				return true;
			if( myAnt.getCol() > lastSeen.getCol() && unSeen.getCol() > lastSeen.getCol() )
				return true;
		}else 
		if( myAnt.getCol() == lastSeen.getCol() ){
			if( myAnt.getRow() < lastSeen.getRow() && unSeen.getRow() < lastSeen.getRow() )
				return true;
			if( myAnt.getRow() > lastSeen.getRow() && unSeen.getRow() > lastSeen.getRow() )
				return true;
		}
		
		return false;
	}

}
