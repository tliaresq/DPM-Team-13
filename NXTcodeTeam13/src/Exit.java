import lejos.nxt.Button;

/**
 * Thread that allows to exit all running programs simply using the esc button at any time
 * @author Cedric
 */
public class Exit extends Thread {
	public void run() {
		//if escape button pressed exit all running program;
		while (true) {
			while (Button.waitForAnyPress() != Button.ID_ESCAPE) ; 
			System.exit(0);
			try { Thread.sleep(10); } catch(Exception e){}
		}
	}
}
