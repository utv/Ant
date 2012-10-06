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
				gameManager.issueOrder(ant, direction);
				break;
			}
		}
	}

	public Tile getAnt4Target(Tile target) {
		Tile tile = null;
		int searchCount = 0;
		int searchLimit = 500;
		/*
		 * BFS here!
		 */
		Queue<Tile> qe = new LinkedList<Tile>();
		qe.add(target);
		
		while(!qe.isEmpty()&& searchCount < searchLimit ){		
			tile = qe.remove();
			if(isMyAnt(tile)){	//found the best ant for this food
				return tile;
			}
			for(Aim direction : Aim.values()){
				Tile neighbor;
				if(gameManager.getIlk(tile, direction).isPassable()){
					neighbor = gameManager.getTile(tile, direction);
					qe.add(neighbor);
				}
				
			}
			searchCount++;
		}
		
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
