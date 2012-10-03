
public class Strategy {
	private MyBot myBot;

	public Strategy(MyBot myBott){
		this.myBot = myBott;
	}
	
	public void simpleMove(){
		Ants ants = myBot.getAnts();
		for (Tile myAnt : ants.getMyAnts()) {
            for (Aim direction : Aim.values()) {
                if (ants.getIlk(myAnt, direction).isPassable()) {
                    ants.issueOrder(myAnt, direction);
                    break;
                }
            }
        }
	}

	
}
