
public class CorrectionBeta extends Thread {
private boolean stop;
private Odometer odo;
private Robot robot;
private LSController ls1,ls2;// ls1 is on port 3 and ls2 on port 4


	public CorrectionBeta(Robot r , Odometer o){
		robot = r;
		odo = o;
		ls1 = odo.lsC1;
		ls2 = odo.lsC2;		
	}
	
	public void run(){
		stop = true;
		while(true){
			if(stop){
				
				try {Thread.sleep(10);} catch (Exception e) {}

			}
			else{
			/*
			 * =============================================================
			 *      DO WHATEVER HERE
			 * =============================================================
			 */
			}
		}
	}
	public void restart(){
		stop = false;
		ls1.restartLS();
		ls2.restartLS();
	}
	public void stop(){
		stop = true;
		odo.lsOff();
		ls1.stopLS();
		ls2.stopLS();
	}

}
