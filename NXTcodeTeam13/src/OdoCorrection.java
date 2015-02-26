import lejos.nxt.Sound;

public class OdoCorrection extends Thread{
	private Odometer odo;
	private boolean stop;
	private double[] lines = {0,30,60,90,120,150,180,210,240,270,300};


	public OdoCorrection( Odometer o){
		stop = true;
		odo = o;
	}

	public void run(){
		stop = true;
		while(true){
			if(stop){
				odo.lsOff();
				try {Thread.sleep(10);} catch (Exception e) {}
				
			}
			else{
				odo.lsOn();
				crossLine();
				update();
			}
		}
	}
	private void crossLine() {
		double black = odo.robot.black;
		double sensorColor = 500;
		 while (sensorColor > black ){
			try { Thread.sleep(10); } catch (Exception e) {}
		}

		
	}
	
	public void update(){
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



	public boolean inBand(double d, double ref){
		if(d < ref+odo.robot.odoCorBand && d > ref-odo.robot.odoCorBand ){
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
