import lejos.nxt.Button;


public class Exit extends Thread {


	public void run() {
		//if escape button pressed exit all running program;
		while (true) {
			while (Button.waitForAnyPress() != Button.ID_ESCAPE)
				; // error in origin file : used to be "Button.waitForPress()"
			System.exit(0);
			try { Thread.sleep(10); } catch(Exception e){}
		}
	}
}
