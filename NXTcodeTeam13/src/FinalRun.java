import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * the final run class gives the set of instructions that are needed to perform the final demo.
 * It include displaying menus to select maps and targets.
 * it activates threads when needed.
 * it calls each method to in the order needed to execute the final run.
 * @author cbonjour
 *
 */
public class FinalRun extends Main {

	public FinalRun(){

	}

	//	test should be false for the final run 
	//	it makes the robot simulate the final on a 2 by 2 board map

	//	problem with the sensors and second localizer
	/**
	 * this method needs to be called when the final run needs to be execuuted
	 * @param test , depending whether this variable is true or not, the robot will perform on a 3 by 3 or 2 by 2 board.
	 */
	public void finalRun(boolean test){
		//Selecting targets
		int target1 = targetSelect(1);
		int target2 = targetSelect(2);
		LCD.clear();
		//once enter is pressed the user should not have to interact with the robot any more
		LCD.drawString("press ENTER to", 0, 0);
		LCD.drawString("GO", 8, 3);
		int buttonPressed = 0;
		while(buttonPressed !=Button.ID_ENTER){
			buttonPressed = Button.waitForAnyPress();
		}

		//starting odometer
		robot.odo.start();
		try {Thread.sleep(1000);} catch (Exception e) {}
		//starting sensors
		robot.odo.lsM.start();
		robot.odo.usCfront.start();
		// localizes
		nav.localizer.alphaLocalize(true);
		//breaks for 2 sc and beeps 3 times 
		nav.qBreak(500);nav.qBreak(500);nav.qBreak(1000);
		//initializes everything needed for the run
		robot.odo.lsR.start();
		robot.odo.usCleft.start();
		robot.odo.usCleft.restartUS();
		robot.speed = 300;
		robot.acc = 700;
		nav.setAccSp(robot.acc, robot.speed);
		robot.odo.correction.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		//travels to intersection of lines 10-10 and relocalizes
		if (test){nav.travelToAlphaRelocalizeCross(0, 6, true, false);}
		else{nav.travelToAlphaRelocalizeCross(10, 10, true, false);}
		nav.qBreak(2000);
		// places itself and shoots at targets
		doTargetInstructions(target1);
		nav.qBreak(2000);
		doTargetInstructions(target2);
		nav.qBreak(2000);
		try {Thread.sleep(1000);} catch (Exception e) {}

		robot.odo.usCleft.restartUS();
		try {Thread.sleep(100);} catch (Exception e) {}

		//travels back to 0-0 an relocalizes
		nav.travelToAlphaRelocalizeCross(0, 0, true, true);
		nav.travelTo(0, 0, false, false);
		nav.pointTo(90);
		// end of run signal ; shuts down after 2 minutes
		nav.qBreak(500);
		nav.qBreak(500);
		nav.qBreak(12000);//2min;
		System.exit(0);
	}
	/**
	 * this method is is equivalent to "finalrun" method but goes through a known path depending on the map instead of going through the middle
	 * 
	 * @param test
	 */
	public void finalMapRun(boolean test){
		//extra map selection
		int mapNumber = mapSelect();
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

		robot.odo.lsM.start();
		robot.odo.usCfront.start();

		nav.localizer.alphaLocalize(true);
		nav.qBreak(500);nav.qBreak(500);nav.qBreak(1000);

		robot.odo.lsR.start();
		robot.odo.usCleft.start();
		robot.odo.usCleft.restartUS();

		robot.speed = 300;
		robot.acc = 700;
		nav.setAccSp(robot.acc, robot.speed);

		robot.odo.correction.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		//intructions to get to target zone and relocalize
		mapToTarget(mapNumber);
		doTargetInstructions(target1);
		doTargetInstructions(target2);
		try {Thread.sleep(1000);} catch (Exception e) {}

		nav.localizer.omegalineLocalizeNE();

		robot.odo.usCleft.start();
		robot.odo.usCleft.restartUS();
		robot.odo.correction.start();
		try {Thread.sleep(100);} catch (Exception e) {}
		//intructions to get back to the origin and relocalize
		mapToOrigin(mapNumber);
		nav.travelTo(0, 0, false, false);
		nav.pointTo(90);

		nav.qBreak(500);
		nav.qBreak(500);
		nav.qBreak(12000);//2min;
		System.exit(0);
	}

	/**
	 * instructions to get from the starting zone to the shooting zone depending on the mapNumber
	 * @param mapNumber
	 */
	private void mapToTarget(int mapNumber) {

		double tile  = 30.48;
		if (mapNumber == 1){
			nav.travelTo(1.5*tile, 1.5*tile, false, true);
			nav.travelTo(4.5*tile, 1.5*tile, false, true);
			nav.travelTo(4.5*tile, -0.5*tile, false, true);
			nav.travelTo(7.5*tile, -0.5*tile, false, true);
			nav.travelTo(7.5*tile, 0.5*tile, false, true);
			nav.travelTo(8.5*tile, 0.5*tile, false, true);
			nav.travelTo(9.5*tile,2.5*tile, false, true);
			nav.travelTo(10.5*tile, 2.5*tile, false, true);
			nav.travelTo(10.5*tile, 8.5*tile, false, true);
		}
		else if (mapNumber == 2){
			nav.travelTo(2.5*tile, 3*tile, false, true);
			nav.travelTo(2.5*tile, 5*tile, false, true);
			nav.travelTo(1.5*tile, 6.5*tile, false, true);
			nav.travelTo(1.5*tile, 8.5*tile, false, true);
			nav.travelTo(-0.5*tile, 8.5*tile, false, true);
			nav.travelTo(-0.5*tile, 10.5*tile, false, true);
			nav.travelTo(9*tile, 10.5*tile, false, true);
		}
		else if (mapNumber == 3){
			nav.travelTo(-0.5*tile, 2*tile, false, true);
			nav.travelTo(-0.5*tile, 4.5*tile, false, true);
			nav.travelTo(2.5*tile, 4.5*tile, false, true);
			nav.travelTo(2.5*tile, 7.5*tile, false, true);
			nav.travelTo(-0.5*tile, 7.5*tile, false, true);
			nav.travelTo(-0.5*tile, 9.5*tile, false, true);
			nav.travelTo(0.5*tile, 9.5*tile, false, true);
			nav.travelTo(0.5*tile, 10.5*tile, false, true);
			nav.travelTo(9*tile, 10.5*tile, false, true);
		}
		nav.travelToRelocalizeCross(9, 9, false);
	}

	/**
	 * instructions to get from the shooting zone to the origin depending on the mapNumber
	 * @param mapNumber
	 */
	private void mapToOrigin(int mapNumber) {
		double tile  = 30.48;
		if (mapNumber == 1){
			nav.travelTo(10.5*tile, 8.5*tile, false, true);
			nav.travelTo(10.5*tile, 2.5*tile, false, true);
			nav.travelTo(9.5*tile,2.5*tile, false, true);
			nav.travelTo(8.5*tile, 0.5*tile, false, true);
			nav.travelTo(7.5*tile, 0.5*tile, false, true);
			nav.travelTo(7.5*tile, -0.5*tile, false, true);
			nav.travelTo(4.5*tile, -0.5*tile, false, true);
			nav.travelTo(4.5*tile, 1.5*tile, false, true);
			nav.travelTo(1.5*tile, 1.5*tile, false, true);
			nav.travelTo(0, 0.5*tile, false, true);	
		}
		else if (mapNumber == 2){
			nav.travelTo(9*tile, 10.5*tile, false, true);
			nav.travelTo(-0.5*tile, 10.5*tile, false, true);
			nav.travelTo(-0.5*tile, 8.5*tile, false, true);
			nav.travelTo(1.5*tile, 8.5*tile, false, true);
			nav.travelTo(1.5*tile, 6.5*tile, false, true);
			nav.travelTo(2.5*tile, 5*tile, false, true);
			nav.travelTo(2.5*tile, 3*tile, false, true);
			nav.travelTo(0, 0.5*tile, false, true);
		}
		else if (mapNumber == 3){
			nav.travelTo(9*tile, 10.5*tile, false, true);
			nav.travelTo(0.5*tile, 10.5*tile, false, true);
			nav.travelTo(0.5*tile, 9.5*tile, false, true);
			nav.travelTo(-0.5*tile, 9.5*tile, false, true);
			nav.travelTo(-0.5*tile, 7.5*tile, false, true);
			nav.travelTo(2.5*tile, 7.5*tile, false, true);
			nav.travelTo(2.5*tile, 4.5*tile, false, true);
			nav.travelTo(-0.5*tile, 4.5*tile, false, true);
			nav.travelTo(-0.5*tile, 2*tile, false, true);
			nav.travelTo(0, 0.5*tile, false, true);
		}
		nav.travelToRelocalizeCross(0, 0, false);
	}


	/**
	 * depending on the target selected "targetNum" (see target documentation), Places te robot accordingly and makes it shoot 3 balls at target
	 * @param targetNum
	 */
	private void doTargetInstructions(int targetNum) {
		double x = 10.0;
		double y = 10.0;
		double angle = 0.0;
		//signal();
		if      (targetNum==  1){x= 9   ;  y= 8.42;  angle=113.6;}
		else if (targetNum==  2){x= 9   ;  y= 9.42;  angle=113.6;}
		else if (targetNum==  3){x= 8   ;  y= 8   ;  angle= 90  ;}
		else if (targetNum==  4){x= 8   ;  y= 9   ;  angle= 90  ;}
		else if (targetNum==  5){x= 9   ;  y= 8   ;  angle= 90  ;}
		else if (targetNum==  6){x= 9   ;  y= 9   ;  angle= 90  ;}
		else if (targetNum==  7){x=10   ;  y= 8   ;  angle= 90  ;}
		else if (targetNum==  8){x=10   ;  y= 9   ;  angle= 90  ;}
		else if (targetNum==  9){x= 9   ;  y= 8.42;  angle= 66.4;}
		else if (targetNum== 10){x= 9   ;  y= 9.42;  angle= 66.4;}
		else if (targetNum== 11){x=10   ;  y= 8.42;  angle= 66.4;}
		else if (targetNum== 12){x=10   ;  y= 9.42;  angle= 66.4;}
		else if (targetNum== 13){x= 8.42;  y= 9   ;  angle=336.4;}
		else if (targetNum== 14){x= 8   ;  y= 8   ;  angle=  0  ;}
		else if (targetNum== 15){x= 8   ;  y= 9   ;  angle=  0  ;}
		else if (targetNum== 16){x= 8   ;  y=10   ;  angle=  0  ;}
		else if (targetNum== 17){x= 8.42;  y= 9   ;  angle= 23.6;}
		else if (targetNum== 18){x= 8.42;  y=10   ;  angle= 23.6;}
		else if (targetNum== 19){x= 9.46;  y= 9.46;  angle= 45  ;}
		else if (targetNum== 20){x= 9.46;  y=10.46;  angle= 45  ;}
		else if (targetNum== 21){x= 9.42;  y= 9   ;  angle=336.4;}
		else if (targetNum== 22){x= 9   ;  y= 8   ;  angle=  0  ;}
		else if (targetNum== 23){x= 9   ;  y= 9   ;  angle=  0  ;}
		else if (targetNum== 24){x= 9   ;  y=10   ;  angle=  0  ;}
		else if (targetNum== 25){x= 9.42;  y= 9   ;  angle= 23.6;}
		else if (targetNum== 26){x= 9.42;  y=10   ;  angle= 23.6;}
		else if (targetNum== 27){x=10.46;  y= 9.46;  angle= 45  ;}
		else if (targetNum== 28){x=10.46;  y=10.46;  angle= 45  ;}
		nav.travelTo(30.48*x, 30.48*y, false, true);
		nav.pointTo(angle);
		nav.qBreak(500);
		crossbow.shoot(3);
		nav.qBreak(500);
	}
	/**
	 * presents a menu to select a map
	 * @return
	 */
	public int mapSelect(){
		int map = 1;
		int button = 0;
		String dis = ("Select Map number: ");
		while(button!=Button.ID_ENTER){
			LCD.clear();
			LCD.drawString(dis, 0, 0);

			if(button==Button.ID_LEFT){
				map--;
			}
			if(button==Button.ID_RIGHT){
				map++;
			}
			LCD.drawInt(map, 8, 3);
			button = Button.waitForAnyPress();	
		}
		return map;
	}
	/**
	 * presents a menu to select a target
	 * @param number
	 * @return
	 */
	public int targetSelect(int number){
		int target = 14;
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

}
