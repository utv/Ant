import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


public class Strategy {
	private MyBot myBot;
	private Ants gameManager;
	private final PathFinder pathFinder = new PathFinder();
	
	//private final Map<Tile, Tile> foodMap = new HashMap<Tile, Tile>(); //<food, ant>

	public Strategy(MyBot myBott){
		this.myBot = myBott;
		
	}
	
	//myBot changes every turns so we have to update it to our Strategy
	private void updateGameState() {
		this.gameManager = myBot.getAnts();
		this.pathFinder.gameManager = this.gameManager;
	}
	
	private void simpleMove(){
		//go to direction N,E,S,W in order, *always go N if passable
		//ants = game manager
		Ants ants = myBot.getAnts();    
		
		for (Tile myAnt : ants.getMyAnts()) {	//myAnt = each ant tile
            for (Aim direction : Aim.values()) {
                if (ants.getIlk(myAnt, direction).isPassable()) {
                    ants.issuePreOrder(myAnt, direction);
                    break;
                }
            }
        }
	}
	
	public void doMainStrategy(){
		updateGameState();
		simpleMove();
		findFood();
		gameManager.issueOrders();

	}	
	
	private void findFood(){
		for(Tile food : gameManager.getFoodTiles()){
			pathFinder.assignAnt2Target(food);
		}
	}

}
