import lejos.nxt.Sound;
/**
 * Corrects the odometer each time a line is crossed if conditions are right
 * @author Cedric
 *
 */
public class OdoCorrection extends Thread{
	private Odometer odo;
	private boolean stop;
	private double[] lines = {0,30.48,60.96,91.44,121.92,152.40,182.88,213.36,243.84,274.32,304.8,335.28,365.76,396.24,426.72,457.20,487.68};

	public OdoCorrection( Odometer o){
		stop = true;
		odo = o;
	}

	public void run(){
		stop = true;
		while(true){
			if(stop){
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{
				crossLine();
				//found a line
				update();
			}
		}
	}
	/**
	 * does nothing until line is crossed
	 */
	private void crossLine() {
		while (odo.getLSState()==false){
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
	/**
	 * 
	 * once a line is crossed at a given point, checks what different lines it could be
	 * if that point is far from any line intersection and close enough to a line it will update x and y accordingly 
	 */
	private void update(){
		double dy = odo.robot.lsDist*Math.sin(odo.getTheta()*3.14159/180);
		double dx = odo.robot.lsDist*Math.cos(odo.getTheta()*3.14159/180);
		double xDetect = odo.getX()+dx;
		double yDetect = odo.getY()+dy;
		double yCrossed = -100;
		double xCrossed = -100;

		for (int i = 0; i < lines.length; i++ ){
			if(inBand(yDetect, lines[i])){yCrossed = lines[i];}
			if(inBand(xDetect, lines[i])){xCrossed = lines[i];}
		}
		if(xCrossed== -100 && yCrossed == -100){
			//Error 
			// or the odometer is too off for apropriate correction
			// or it has detected a line it shouldn't have
		}
		if(xCrossed!= -100 && yCrossed != -100){
			// the robot is close to a line intersection
			// we will not take the risk to update since we don't know wether to update x or y;
		}
		if(xCrossed!= -100 && yCrossed == -100){
			odo.setX(xCrossed-dx);
			Sound.beep();
		}
		if(xCrossed== -100 && yCrossed != -100){
			odo.setY(yCrossed - dy);
			Sound.beep();
		}
	}


	/**
	 * check if a certain value "d" is within a certain range of a reference value
	 * @param d
	 * @param ref
	 * @return
	 */
	private boolean inBand(double d, double ref){
		if(d < ref+odo.robot.odoCorBand && d > ref-odo.robot.odoCorBand ){
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * restarts the odometry correction
	 */
	public void restart(){
		stop = false;
		odo.lsC.restartLS();
	}
	/**
	 * Stops odometry correction
	 * Does not stop the thread from running
	 */
	public void stop(){
		stop = true;
		odo.lsC.stopLS();
	}
}
