import lejos.nxt.NXTRegulatedMotor;

public class LauncherController {

	private NXTRegulatedMotor motor;
	/**
	 * 
	 * @param r
	 */
	public LauncherController(Robot r) {
		motor = r.armMotor;		
		motor.setAcceleration(0);
		motor.forward();
		motor.setSpeed(0);	
	}

	/**
	 * shoots pingpong balls a number of times
	 * @param times
	 */
	public void shoot(int times) {
		for (int i = 0; i < times ;  i++){
			shoot();
			try {
				Thread.sleep(700);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Shoots 1 pingpong ball at 1m50 exactly
	 * Angle to be determined
	 * The Arm has to be in a good starting position
	 */
	public void shoot() {
		int shootAcc = 10000;
		int shootSpeed = 900;
		int reloadAcc = 10000;
		int reloadSpeed = 100;
		int shootAngle = 180;
		int reloadAngle = 90;

		motor.setAcceleration(shootAcc);
		motor.forward();
		motor.setSpeed(shootSpeed);
		motor.rotate(shootAngle);
		motor.setAcceleration(reloadAcc);
		motor.rotate(-shootAngle);
		motor.setSpeed(reloadSpeed);
		motor.rotate(-reloadAngle);
		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}
		motor.rotate(reloadAngle);
	}

}
