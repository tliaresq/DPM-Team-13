import lejos.nxt.Sound;

public class OdoCorrection extends Thread{
	private Robot robot;
	private Odometer odo;
	private boolean stop;
	private double[] lines = {0,30,60,90,120,150,180,210,240,270,300};
	private double lsDist;
	private double width;


	public OdoCorrection(Robot r, Odometer o){
		stop = true;

		robot = r;
		odo = o;
		lsDist = r.lsDist;
		width = r.odoCorBand;
	}

	public void run(){
		while(true){
			if(stop){
				odo.lsStop();
				try {Thread.sleep(10);} catch (Exception e) {}
				
			}
			else{
				odo.lsStart();
				crossLine();
				update();
			}
		}
	}
	private void crossLine() {
		int counter = 0;
		int maxCount = 5;
		double black = robot.black;
		double sensorColor = 500;
		do {
			sensorColor = odo.getSensorColor();
			if (sensorColor < black && sensorColor> -1) {
				counter++;
			} else {
				counter = 0;
			}
			try { Thread.sleep(10); } catch (Exception e) {}
		} while (counter < maxCount);

		
	}
	
	public void update(){
		double dy = lsDist*Math.sin(odo.getTheta()*3.14159/180);
		double dx = lsDist*Math.cos(odo.getTheta()*3.14159/180);

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



	public boolean inBand(double d, double ref){
		if(d < ref+width && d > ref-width ){
			return true;
		}
		else {
			return false;
		}
	}

	public void restart(){
		stop = false;
	}
	public void stop(){
		stop = true;
	}


}
