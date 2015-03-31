import lejos.nxt.Sound;
/**
 * Corrects the odometer each time a line is crossed if conditions are right
 * @author Cedric
 *
 */
public class OdoCorrection extends Thread{
	private Odometer odo;
	public boolean goingStraight;
	private boolean stop;
	private double[] lines = {0,30.48,60.96,91.44,121.92,152.40,182.88,213.36,243.84,274.32,304.8,335.28,365.76,396.24,426.72,457.20,487.68};


	private int[] lX,lY,rX, rY;

	public OdoCorrection( Odometer o){
		stop = true;
		odo = o;
		lX = new int[2];
		lY = new int[2];
		rX= new int[2];
		rY = new int[2];
	}

	public void run(){
		stop = true;
		while(true){
			if(stop){
				try {Thread.sleep(10);} catch (Exception e) {}
			}
			else{
				if(crossLine()){ //found a line
					updateLeft();
				}
				else{
					updateRight();
				}
				double travelledDist;
				if(lX[0]>0 && rX[0]>0){
					travelledDist = rX[0] - lX[0];
					if(odo.getTheta()>90 && odo.getTheta()<270){
						if (travelledDist<0){
							odo.setTheta(270 -Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
						else{
							odo.setTheta(90+Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
							
					}
					else{
						if (travelledDist>0){
							odo.setTheta(270 +Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
						else{
							odo.setTheta(90-Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
					}
					
				}					
				if(lY[0]>0 && rY[0]>0){
					travelledDist = rY[0] - lY[0];
					if(odo.getTheta()>180 && odo.getTheta()<360){
						if (travelledDist<0){
							odo.setTheta(180 -Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
						else{
							odo.setTheta(0+Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
							
					}
					else{
						if (travelledDist>0){
							odo.setTheta(180 +Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
						else{
							odo.setTheta(0-Math.abs(Math.atan(odo.robot.lsDistR/ travelledDist)));
						}
					}
				}
			}
		}
	}
	/**
	 * does nothing until line is crossed
	 */
	private boolean crossLine() {
		boolean left = false;
		boolean right = false;
		while (!(left==true || right==true)){
			left = odo.isLineM();
			right = odo.isLineR();
			if(!goingStraight){
				lX[0] = -1;
				lX[1] = -1;
				lY[0] = -1;
				lY[1] = -1;
				rX[0] = -1;
				rX[1] = -1;
				rY[0] = -1;
				rY[1] = -1;
			}
			try { Thread.sleep(10); } catch (Exception e) {}
		}

		if (left == true ){
			return true;
		}
		else{
			return false;
		}


	}
	/**
	 * 
	 * once a line is crossed at a given point, checks what different lines it could be
	 * if that point is far from any line intersection and close enough to a line it will update x and y accordingly 
	 */
	private void updateLeft(){
		double dy = odo.robot.lsDist*Math.sin(Math.toRadians(odo.getTheta()));
		double dx = odo.robot.lsDist*Math.cos(Math.toRadians(odo.getTheta()));
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
			// or the odometer is too off for appropriate correction
			// or it has detected a line it shouldn't have
		}
		if(xCrossed!= -100 && yCrossed != -100){
			// the robot is close to a line intersection
			// we will not take the risk to update since we don't know whether to update x or y;
		}
		if(xCrossed!= -100 && yCrossed == -100){
			odo.setX(xCrossed-dx);
			lX[0] = odo.robot.leftMotor.getTachoCount();
			lX[1] = odo.robot.rightMotor.getTachoCount();			
			Sound.beep();

		}
		if(xCrossed== -100 && yCrossed != -100){
			odo.setY(yCrossed - dy);
			lY[0] = odo.robot.leftMotor.getTachoCount();
			lY[1] = odo.robot.rightMotor.getTachoCount();
			Sound.beep();
		}
	}
	private void updateRight(){
		double dy = odo.robot.lsrDist*Math.sin(Math.toRadians(odo.getTheta() - odo.robot.lsrAngle));
		double dx = odo.robot.lsrDist*Math.cos(Math.toRadians(odo.getTheta()- odo.robot.lsrAngle));
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
			// or the odometer is too off for appropriate correction
			// or it has detected a line it shouldn't have
		}
		if(xCrossed!= -100 && yCrossed != -100){
			// the robot is close to a line intersection
			// we will not take the risk to update since we don't know whether to update x or y;
		}
		if(xCrossed!= -100 && yCrossed == -100){
			rX[0] = odo.robot.leftMotor.getTachoCount();
			rX[1] = odo.robot.rightMotor.getTachoCount();
			odo.setX(xCrossed-dx);
			Sound.beep();
		}
		if(xCrossed== -100 && yCrossed != -100){
			odo.setY(yCrossed - dy);
			rY[0] = odo.robot.leftMotor.getTachoCount();
			rY[1] = odo.robot.rightMotor.getTachoCount();
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
	}
	/**
	 * Stops odometry correction
	 * Does not stop the thread from running
	 */
	public void stop(){
		stop = true;
	}
}
