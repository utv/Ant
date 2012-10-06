import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


public class PathFinder {

	protected Ants gameManager;
	
	
	public PathFinder(){
		
	}

	/*
	 * Move ant to any kinds of tile we want
	 */
	private void moveAnt2Target(Tile ant, Tile target){
		List<Aim> directions = gameManager.getDirections(ant, target);
		for(Aim direction : directions){
			if(gameManager.getIlk(ant, direction).isPassable()){
				gameManager.issuePreOrder(ant, direction);
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
			if(isMyAnt(tile)){	//found best ant for this food
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

	public Tile getAnt4TargetNoMap(Tile target) {
		Tile tile = null;
		int searchCount = 0;
		int searchDepth = 500;
		/*
		 * BFS here!
		 */
		Queue<Tile> qe = new LinkedList<Tile>();
		//HashSet<Tile> visitedTile = new HashSet<Tile>();
		qe.add(target);
		
		while(!qe.isEmpty()&& searchCount < searchDepth ){		
			tile = qe.remove();
			if(isMyAnt(tile)){	//found the best ant for this food
				//DEBUG
				System.out.println("number of search = " + searchCount);
				return tile;
			}
			for(Aim direction : Aim.values()){
				if(isPassAbleTile(tile, direction)){
					Tile neighbor = gameManager.getTile(tile, direction);
					qe.add(neighbor);
				}
			}
			searchCount++;
		}
		//DEBUG
		System.out.println("number of search = " + searchCount);
		return null;	//can't find any ants for this food
	}

	private boolean isMyAnt(Tile tile) {
		return gameManager.map[tile.getRow()][tile.getCol()].equals(Ilk.MY_ANT);
	}

	public void assignAnt2Target(Tile target){
		Tile ant = getAnt4Target(target);
		//if there is no ant near this food, do nothing
		if(ant != null)
			moveAnt2Target(ant, target);
	}

}