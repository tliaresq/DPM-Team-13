import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;


public class FinalRun extends Main {
	
	public FinalRun(){
		
	}



//	test should be false for the final run 
//	it makes the robot simulate the final on a 2 by 2 board map
	public void finalRun(boolean test){
		int target1 = targetSelect(1);
		int target2 = targetSelect(2);
		LCD.clear();
		LCD.drawString("press ENTER to", 0, 0);

		LCD.drawString("GO", 8, 3);
		int buttonPressed = 0;
		while(buttonPressed !=Button.ID_ENTER){
			buttonPressed = Button.waitForAnyPress();
		}
		robot.odo.start();
		try {Thread.sleep(1000);} catch (Exception e) {}
		nav.localizer.alphaLocalize();
		nav.qBreak(500);nav.qBreak(500);nav.qBreak(1000);
		robot.odo.lsR.start();
		robot.odo.usCleft.start();
		robot.odo.usCleft.restartUS();
		robot.odo.correction.start();
		robot.odo.correction.restart();
		robot.speed = 300;
		robot.acc = 700;
		nav.setAccSp(robot.acc, robot.speed);
		if (test){nav.travelToRelocalizeCross(6, 6, true);}
		else{nav.travelToRelocalizeCross(9, 9, true);}
		doTargetInstructions(target1);
		doTargetInstructions(target2);
		if (test){nav.travelToRelocalizeCross(6, 6, true);}
		else{nav.travelToRelocalizeCross(9, 9, true);}
		nav.travelToRelocalizeCross(6, 6, false);
		nav.travelToRelocalizeCross(0, 0, true);
		nav.qBreak(500);
		nav.qBreak(500);
		nav.qBreak(12000);//2min;
		System.exit(0);
	}

	private void doTargetInstructions(int targetNum) {
		if (targetNum == 1){

		}
		else if (targetNum == 2){

		}
		else if (targetNum == 3){

		}
		else if (targetNum == 4){

		}
		else if (targetNum == 5){

		}
		else{

		}
		
		crossbow.shoot(3);

	}

	public int targetSelect(int number){
		int target = 0;
		int button = 0;
		String dis = ("target number "+number);
		while(button!=Button.ID_ENTER){
			LCD.clear();
			LCD.drawString(dis, 0, 0);
			
			if(button==Button.ID_LEFT){
				target--;
			}
			if(button==Button.ID_RIGHT){
				target++;
			}
			LCD.drawInt(target, 8, 3);
			button = Button.waitForAnyPress();	
		}
		return target;
	}
	
	private void beep(int n ){
		for(int i = 0 ; i <n;i++){
			Sound.beep();
			try {Thread.sleep(500);} catch (Exception e) {}

			
		}
	}


}
