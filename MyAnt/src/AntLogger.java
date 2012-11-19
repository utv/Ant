import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class AntLogger {
	private PrintWriter out;
	private Logger myLogger = Logger.getLogger("AntLogger");
	
	public AntLogger() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//String filename = "Ant_Log_" + dateFormat.format(new Date()) + ".log";
		String filename = "ant.log";
		try {
			FileHandler fileHandler = new FileHandler(filename);
			myLogger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);  
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void debug(String msg){
		myLogger.info(msg);
	}
	
	public void log(String msg){
		myLogger.log(Level.FINE, msg);
	}
	
}
