import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;

public class LauncherController {
	private NXTRegulatedMotor motor;

	public LauncherController(NXTRegulatedMotor m) {
		// TODO Auto-generated constructor stub
		motor = m;
		
		motor.setAcceleration(0);
		motor.forward();
		motor.setSpeed(0);
	

	}

	public void ballisticsMenu() {
		LCD.clear();
		LCD.drawString("left = shoot once", 0, 0);
		LCD.drawString("right = shoot 6", 0, 1);
		int option = 0;
		while (option != Button.ID_ESCAPE) {
			option = 0;
			option = Button.waitForAnyPress();
			

			if (option == Button.ID_LEFT) {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				shoot(1);
				
			}
			if (option == Button.ID_RIGHT) {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				shoot(6);
				
			}
		}
	}

	public void shoot(int times) {
		for (int i = 0; i < times ;  i++){
			shoot();
			try {
				Thread.sleep(700);
			} catch (Exception e) {
			}
		}
	}

	public void shoot() {
		int shootAcc = 10000;
		int shootSpeed = 900;
		int reloadAcc = 10000;
		int reloadSpeed = 100;
		int angle = 180;
		int reloadAngle = 90;
		
		motor.setAcceleration(shootAcc);
		motor.forward();
		motor.setSpeed(shootSpeed);
		motor.rotate(180);
		motor.setAcceleration(reloadAcc);
		motor.rotate(-180);
		motor.setSpeed(reloadSpeed);
		motor.rotate(-reloadAngle);
		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}
		motor.rotate(reloadAngle);
	}

}
