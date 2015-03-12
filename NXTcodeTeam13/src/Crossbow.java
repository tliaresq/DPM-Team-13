import lejos.nxt.NXTRegulatedMotor;


public class Crossbow {
	private NXTRegulatedMotor motor;
	public Crossbow(Robot r){
		motor = r.armMotor;	
	}
	
	
	
	public void shoot(int n){
		for(int i = 0; i< n; i++ ){
			shoot();
		}
	}
	
	
	
	private void shoot(){
		motor.setAcceleration(6000);
		motor.setSpeed(200);
		motor.rotate(360);
	
	}

}
