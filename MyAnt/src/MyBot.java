import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Starter bot implementation.
 */
public class MyBot extends Bot {
    private Strategy strategy;	//my stategy
    //private static AntLogger logger;
	
    public MyBot(){
    	//logger = new AntLogger();
    	//strategy = new Strategy(this, logger);
    	strategy = new Strategy(this);
    	
    }
	/**
     * Main method executed by the game engine for starting the bot.
     * 
     * @param args command line arguments
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {    
    	new MyBot().readSystemInput();
    	
    }
    
    /**
     * For every ant check every direction in fixed order (N, E, S, W) and move it if the tile is
     * passable.
     */
    @Override
    public void doTurn() {
    	strategy.doMainStrategy();
    }
}
