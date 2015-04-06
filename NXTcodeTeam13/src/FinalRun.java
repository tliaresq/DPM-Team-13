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
		if (test){nav.travelToRelocalizeCross(5, 5, true);}
		else{nav.travelToRelocalizeCross(9, 9, true);}
		doTargetInstructions(target1, test);
		doTargetInstructions(target2, test);
		if (test){nav.travelToRelocalizeCross(5, 5, true);}
		else{nav.travelToRelocalizeCross(9, 9, true);}	
		nav.travelToRelocalizeCross(0, 0, true);
		nav.qBreak(500);
		nav.qBreak(500);
		nav.qBreak(12000);//2min;
		System.exit(0);
	}

	private void doTargetInstructions(int targetNum, boolean test) {
		double i = 0.0;
		double x = 0.0;
		double y = 0.0;
		double angle = 0.0;
		if(test)
		{
			i = 4.0;
		}
		
		if      (targetNum==  1){x= 9   -i;  y= 8.42-i;  angle=113.6;}
		else if (targetNum==  2){x= 9   -i;  y= 9.42-i;  angle=113.6;}
		else if (targetNum==  3){x= 8   -i;  y= 8   -i;  angle= 90  ;}
		else if (targetNum==  4){x= 8   -i;  y= 9   -i;  angle= 90  ;}
		else if (targetNum==  5){x= 9   -i;  y= 8   -i;  angle= 90  ;}
		else if (targetNum==  6){x= 9   -i;  y= 9   -i;  angle= 90  ;}
		else if (targetNum==  7){x=10   -i;  y= 8   -i;  angle= 90  ;}
		else if (targetNum==  8){x=10   -i;  y= 9   -i;  angle= 90  ;}
		else if (targetNum==  9){x= 9   -i;  y= 8.42-i;  angle= 66.4;}
		else if (targetNum== 10){x= 9   -i;  y= 9.42-i;  angle= 66.4;}
		else if (targetNum== 11){x=10   -i;  y= 8.42-i;  angle= 66.4;}
		else if (targetNum== 12){x=10   -i;  y= 9.42-i;  angle= 66.4;}
		else if (targetNum== 13){x= 8.42-i;  y= 9   -i;  angle=336.4;}
		else if (targetNum== 14){x= 8   -i;  y= 8   -i;  angle=  0  ;}
		else if (targetNum== 15){x= 8   -i;  y= 9   -i;  angle=  0  ;}
		else if (targetNum== 16){x= 8   -i;  y=10   -i;  angle=  0  ;}
		else if (targetNum== 17){x= 8.42-i;  y= 9   -i;  angle= 23.6;}
		else if (targetNum== 18){x= 8.42-i;  y=10   -i;  angle= 23.6;}
		else if (targetNum== 19){x= 9.46-i;  y= 9.46-i;  angle= 45  ;}
		else if (targetNum== 20){x= 9.46-i;  y=10.46-i;  angle= 45  ;}
		else if (targetNum== 21){x= 9.42-i;  y= 9   -i;  angle=336.4;}
		else if (targetNum== 22){x= 9   -i;  y= 8   -i;  angle=  0  ;}
		else if (targetNum== 23){x= 9   -i;  y= 9   -i;  angle=  0  ;}
		else if (targetNum== 24){x= 9   -i;  y=10   -i;  angle=  0  ;}
		else if (targetNum== 25){x= 9.42-i;  y= 9   -i;  angle= 23.6;}
		else if (targetNum== 26){x= 9.42-i;  y=10   -i;  angle= 23.6;}
		else if (targetNum== 27){x=10.46-i;  y= 9.46-i;  angle= 45  ;}
		else if (targetNum== 28){x=10.46-i;  y=10.46-i;  angle= 45  ;}
		nav.travelTo(30.48*x, 30.48*y, false, true);
		nav.pointTo(angle);
		nav.qBreak(500);
		crossbow.shoot(3);
		nav.qBreak(500);
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
