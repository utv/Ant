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
	private int turn = 0;
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
			//if(!gameManager.isAssignedAnt(myAnt))	
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
		
		//turn++;
		//logger.debug("----------------------- turn " + turn + "");
		//logger.debug("+++++++++++++++++ number of ants = " + gameManager.getMyAnts().size() );
		//logger.debug("+++++++++++++++++ number of ants for unseen tile = " + gameManager.getAnt2Targets().size() );
	}	
	
	private void explore() {
		for (Tile myAnt : gameManager.getMyAnts()) {	//myAnt = each ant tile
			
				for(Tile myHill : gameManager.getMyHills() ){
					if(myAnt.compareTo(myHill) == 0){	//my ant is on my hill
						simpleMove(myAnt);
					}else{
						eachAntExplore(myAnt);
					}
				}
			
        }
	}

	private void eachAntExplore(Tile myAnt) {
		for(Aim direction : Aim.values()){
			Tile lastSeen = gameManager.getTile(myAnt, direction);
			if( gameManager.getLastSeenAnts().contains( lastSeen ) ){
				pathFinder.exploreByLastSeenTile(myAnt, lastSeen);
				break;
			}
		}
	}



	private void findFood(){
		for(Tile food : gameManager.getFoodTiles())
			//pathFinder.assignAnt2Food(food);
			pathFinder.assignAnt2Target(food);
	}

}
