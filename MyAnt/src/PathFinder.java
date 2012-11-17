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
	private void moveAnt2Target(Tile ant, Tile target){
		
		List<Aim> directions = gameManager.getDirections(ant, target);
		for(Aim direction : directions){
			if(gameManager.getIlk(ant, direction).isPassable()){
				gameManager.issueOrder(ant, direction);
				//gameManager.setAnt2Target(ant, target);
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

	public boolean assignAnt2Food(Tile food){
		Tile ant = assignAnt2Target(food);
		if( ant != null){
			gameManager.setAnt2Food(ant);
			return true;
		}else
			return false;
	}
	
	public Tile assignAnt2Target(Tile target){

		/*old version
		 * 	Tile ant = getAnt4Target(target);
			moveAnt2Target(ant, target);
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
						moveAnt2Target(neighbor, tile);
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
						moveAnt2Target(myAnt, tile);
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
	
	public Tile getUnseenTileNearMyAnt(Tile myAnt){
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
			if(isVisibleTile(tile) == false)
				return tile;
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
		return null;
	}
	
	public Tile moveAnt2UnseenTile(Tile myAnt) {
		Tile target = getUnseenTileNearMyAnt(myAnt);
		assignAnt2Target( myAnt, target );
		return target;
	}
	
	public void assignAnt2UnseenTile(Tile myAnt) {
		Tile target = getUnseenTileNearMyAnt(myAnt);
		assignAnt2Target( myAnt, target );
		gameManager.setAnt2Target(myAnt, target);
	}

}
