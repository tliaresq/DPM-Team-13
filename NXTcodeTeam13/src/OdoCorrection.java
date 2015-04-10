import lejos.nxt.*;
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
	private double[] detLine = {-1.0, -1.0, -1.0};
	private double[] pos = {-1.0, -1.0, 0.0};
	

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
//				found a line
				if(odo.isLineM() && odo.isLineR())
				{
					update(2.0);
				}
				else if(odo.isLineM())
				{
					update(0.0);
				}
				else if(odo.isLineR())
				{
					update(1.0);
				}
			}
		}
	}
	/**
	 * does nothing until line is crossed
	 */
	private void crossLine() {
		while (odo.isLineM()==false && odo.isLineR()==false){
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
	/**
	 * 
	 * once a line is crossed at a given point, checks what different lines it could be
	 * if that point is far from any line intersection and close enough to a line it will update x and y accordingly 
	 */
	private void update(double sen){
		double lsDist = 0.0;
		double dt = 0.0;
		if(sen==0.0 || sen==2.0)
		{
			lsDist = odo.robot.lsDist;
			dt = 0.0;
		}
		else if(sen==1.0)
		{
			lsDist = Math.sqrt((odo.robot.lsDist*odo.robot.lsDist)+(odo.robot.lsDistR*odo.robot.lsDistR));
			dt = -Math.toDegrees(Math.atan(odo.robot.lsDistR/odo.robot.lsDist));
		}
		double dy = lsDist*Math.sin(Math.toRadians(odo.getTheta()+dt));
		double dx = lsDist*Math.cos(Math.toRadians(odo.getTheta()+dt));
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
			if(sen==2.0 && odo.getTheta()<=195.0 && odo.getTheta()>=165.0)
			{
				odo.setTheta(180.0);
			}
			else if(sen==2.0 && (odo.getTheta()>345.0 || odo.getTheta()<15.0))
			{
				odo.setTheta(0.0);
			}
			else
			{
				thetaUpdate(sen, 1.0, xCrossed);
			}
				odo.setX(xCrossed-dx);
				Sound.beep();
		}
		if(xCrossed== -100 && yCrossed != -100){
			if(sen==2.0 && odo.getTheta()<=105.0 && odo.getTheta()>=75.0)
			{
				odo.setTheta(90.0);
			}
			else if(sen==2.0 && odo.getTheta()>255.0 && odo.getTheta()<255.0)
			{
				odo.setTheta(270.0);
			}
			else
			{
				thetaUpdate(sen, 0.0, yCrossed);
			}
				odo.setY(yCrossed - dy);
				Sound.beep();
		}
	}
	
	private void thetaUpdate(double sen, double axis, double coord)
	{
			if(detLine[0]!=sen && detLine[1]==axis && detLine[2]==coord && Math.abs(pos[0]-odo.getTheta())<=1)
			{
				double k = odo.robot.lsDistR;
				double d = (Math.toRadians(Math.abs(Motor.C.getTachoCount()-pos[1])))*odo.robot.leftWradius;
				
				if(axis==0.0 && odo.getTheta()<=180.0 && sen==1.0)
				{
					double theta = Math.toDegrees(Math.atan(k/d));
					odo.setTheta(theta);
				}
				else if(axis==0.0 && odo.getTheta()<=180.0 && sen==0.0)
				{
					double theta =180.0 - Math.toDegrees(Math.atan(k/d));
					odo.setTheta(theta);
				}
				else if(axis==0.0 && odo.getTheta()>=180.0 && sen==1.0)
				{
					double theta = Math.toDegrees(Math.atan(k/d))+180.0;
					odo.setTheta(theta);
				}
				else if(axis==0.0 && odo.getTheta()>=180.0 && sen==0.0)
				{
					double theta = 360.0 - Math.toDegrees(Math.atan(k/d));
					odo.setTheta(theta);
				}
				else if(axis==1.0 && (odo.getTheta()<=90.0 || odo.getTheta()>=270.0) && sen==1.0)
				{
					double theta = Math.toDegrees(Math.atan(k/d))+270.0;
					odo.setTheta(theta);
				}
				else if(axis==1.0 && (odo.getTheta()<=90.0 || odo.getTheta()>=270.0) && sen==0.0)
				{
					double theta = 90.0 - Math.toDegrees(Math.atan(k/d));
					odo.setTheta(theta);
				}
				else if(axis==1.0 && (odo.getTheta()>=90.0 && odo.getTheta()<=270.0) && sen==1.0)
				{
					double theta = Math.toDegrees(Math.atan(k/d))+90.0;
					odo.setTheta(theta);
				}
				else if(axis==1.0 && (odo.getTheta()>=90.0 && odo.getTheta()<=270.0) && sen==0.0)
				{
					double theta = 270.0 - Math.toDegrees(Math.atan(k/d));
					odo.setTheta(theta);
				}
				pos[2]=1.0;
			}
			else
			{
				detLine[0]=sen;
				detLine[1]=axis;
				detLine[2]=coord;
				
				pos[0]=odo.getTheta();
				pos[1]=Motor.C.getTachoCount();
				pos[2]=0.0;
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
	
	private void signal(){
		LCD.clear();
		LCD.drawString("################################", 0, 0);
		LCD.drawString("################################", 0, 1);
		LCD.drawString("################################", 0, 2);
		LCD.drawString("################################", 0, 3);
		LCD.drawString("################################", 0, 5);
		LCD.drawString("################################", 0, 6);
		LCD.drawString("################################", 0, 7);
		
	}
}


