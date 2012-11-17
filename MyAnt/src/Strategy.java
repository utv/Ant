import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Strategy {
	private MyBot myBot;
	private Ants gameManager;
	private final PathFinder pathFinder = new PathFinder();
	private final Set<Aim> visitedDirection = new HashSet<Aim>();	//for clockwise move order from my hill
	private final AntLogger logger;
	//private final Map<Tile, Tile> foodMap = new HashMap<Tile, Tile>(); //<food, ant>

	public Strategy(MyBot myBott, AntLogger antlog){
		this.myBot = myBott;
		logger = antlog;
	}
	
	/*
	 * Game states get updated every turns by starter pack.
	 * This method updates game state to pathFinder
	 */
	private void updateGameState() {
		this.gameManager = myBot.getAnts();
		this.pathFinder.gameManager = this.gameManager;
	}
	
	private void moveAllAnts(){
		//go to direction N,E,S,W in order, *always go N if passable
		//ants = game manager
		//Ants ants = myBot.getAnts();    
		
		for (Tile myAnt : gameManager.getMyAnts()) {	//myAnt = each ant tile
			if(!gameManager.isAssignedAnt(myAnt))	
				simpleBasicMove(myAnt);
				//simpleMove(myAnt);
        }
	}
	
	private void simpleBasicMove(Tile myAnt){
		for (Aim direction : Aim.values()) {
        	if (gameManager.getIlk(myAnt, direction).isPassable()) {
                gameManager.issueOrder(myAnt, direction);
                break;
            }
        }
    }
	
	
	private void simpleMove(Tile myAnt){
		if(visitedDirection.size() == Aim.values().length)
        	visitedDirection.clear();
		
		for (Aim direction : Aim.values()) {
            if(!visitedDirection.contains(direction)){
            	if (gameManager.getIlk(myAnt, direction).isPassable()) {
                    gameManager.issueOrder(myAnt, direction);
                    visitedDirection.add(direction);
                    break;
                }
            }
        }
	}
	
	public void doMainStrategy(){
		updateGameState();
		findFood();
		explore();
		
	}	
	
	private void explore() {
		for (Tile myAnt : gameManager.getMyAnts()) {	//myAnt = each ant tile
			if(!gameManager.isAssignedAnt(myAnt)){
				for(Tile myHill : gameManager.getMyHills() ){
					if(myAnt.compareTo(myHill) == 0){	//my ant is on my hill
						simpleMove(myAnt);
					}else{
						eachAntExplore(myAnt);
					}
				}
			}
        }
	}

	private void eachAntExplore(Tile myAnt) {
		//my ant is not no my hill and not assigned to any target
		if( gameManager.getAnt2Targets().containsKey(myAnt) == false ){
			//pathFinder.assignAnt2UnseenTile(myAnt);
			Tile target = pathFinder.moveAnt2UnseenTile(myAnt);
			gameManager.setAnt2Target(myAnt, target);
			
			logger.debug("eachAntExplore:: assigns unseen tile to ant row = " + 
			myAnt.getRow() + "col = " + myAnt.getCol() + " to tile row = " + target.getRow() + ", col = " + target.getCol() );
		}else{
		//already assigned
			logger.debug("eachAntExplore::assigned:++++++++++++");
			if( myAnt.compareTo((Tile) gameManager.getAnt2Targets().get(myAnt)) == 0  ){
				//ant reaches its target
				gameManager.getAnt2Targets().remove(myAnt);
				Tile target = pathFinder.moveAnt2UnseenTile(myAnt);
				gameManager.setAnt2Target(myAnt, target);
				logger.debug("eachAntExplore:: reached the target");
			}else{
				//ant hasn't reached its target, then go to the target
				pathFinder.moveAnt2UnseenTile(myAnt);
				logger.debug("eachAntExplore:: keep going to the target");
			}
		}
	}



	private void findFood(){
		for(Tile food : gameManager.getFoodTiles())
			pathFinder.assignAnt2Food(food);
	}

}
