package plott3r_V1_solved;

import lejos.hardware.Sound;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import positions.Position2D;
import positions.Position3D;

public class Roboter {
//	public static void main(String args[]) {
//		try {
//			Roboter roboter = new Roboter();
//			Sound.beep();
//			//roboter.zAchse.getMotor().rotate(90);
//			roboter.moveToHomePosition(true);
//			//roboter.moveToPosition(new Position2D(80, 100), 100);
//			Delay.msDelay(500);
//			roboter.moveFromCurrentPosition(new Position3D(90,0,false),100);
//			for(int i = 0; i < 12; i++) {
//				
//			roboter.polygon(i, 20);
//			}
//			//roboter.drawCircle(60);
////			roboter.bereitePapierVor();
////			roboter.moveToPosition(new Position2D(180, 0), 100);
//			Delay.msDelay(1000);
////			roboter.entfernePapier();
////			roboter.moveToHomePosition();
//			roboter.zAchse.deaktiviere();
//			Sound.twoBeeps();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private Position3D currentPosition;

	private MultiPositionAchse xAchse = new MultiPositionAchse(new TouchSensor(SensorPort.S1), MotorPort.C, Einbaurichtung.REGULAER, new Reifen(40.0), new Zahnradsatz(new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_KLEIN), new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_GROSS)));
	private MultiPositionAchse yAchse = new MultiPositionAchse(new LichtSensor(SensorPort.S3), MotorPort.B, Einbaurichtung.REGULAER, new Reifen(43.2), new Zahnradsatz(new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_KLEIN), new Zahnrad(Zahnrad.ANZAHL_ZAEHNE_GROSS)));
	private DualPositionAchse zAchse = new DualPositionAchse(null, MotorPort.A, Einbaurichtung.REGULAER, null, null);

	public Roboter() {

	}

	public void bereitePapierVor() throws InterruptedException {
		zAchse.deaktiviere();
		yAchse.setSpeed(50);
		while (!yAchse.isSensorAktiv()) {
			yAchse.forward();
		}
		yAchse.stop();
	}

	public void entfernePapier() throws InterruptedException {
		zAchse.deaktiviere();
		yAchse.setSpeed(Integer.MAX_VALUE);
		yAchse.backward(2000);
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		System.exit(0);
	}

	public Position3D getCurrentPosition() {
		return this.currentPosition;
	}

	public MultiPositionAchse getXAchse() {
		return this.xAchse;
	}

	public MultiPositionAchse getYAchse() {
		return this.yAchse;
	}
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void moveToHomePosition() throws InterruptedException {
		moveToHomePosition(false);
	}
	/**
	 * 
	 * @param calibrateY
	 * @throws InterruptedException
	 */
	public void moveToHomePosition(boolean calibrateY) throws InterruptedException {
		zAchse.deaktiviere();
		if(calibrateY) {
			yAchse.setSpeed(50);
			while (!yAchse.isSensorAktiv()) {
				yAchse.forward();
			}
			yAchse.stop();
			
		}
		xAchse.setSpeed(50);
		while (!xAchse.isSensorAktiv()) {
			xAchse.backward();
		}
		xAchse.stop();
//		xAchse.forward();
//		Delay.msDelay(200);
//		xAchse.stop();
		this.currentPosition = new Position3D(0, 0, false);
		this.resetTachoCounts();
		if(calibrateY) {
		this.moveToPosition(new Position2D(10, -10), 50);
		}
		else {
			this.moveToPosition(new Position2D(10, 0), 50);
			}
		this.currentPosition = new Position3D(0, 0, false);
		this.resetTachoCounts();
	}
	


	public void moveToPosition(Position2D position2D, int mmSec) throws InterruptedException {
		this.moveToPosition(new Position3D(position2D, this.zAchse.isAktiv()), mmSec);
	}

	public void moveToPosition(Position3D position, int mmSec) throws InterruptedException {
		if (position.isZ())
			this.zAchse.aktiviere();
		else
			this.zAchse.deaktiviere();

		double deltaX =  position.getX() - currentPosition.getX();
		double deltaY =  position.getY() - currentPosition.getY();
		double hypo = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		double time = hypo / mmSec;

		xAchse.getMotor().synchronizeWith(yAchse.getMotor());

		xAchse.setSpeed(deltaX / time);
		yAchse.setSpeed(deltaY / time);

		xAchse.getMotor().startSynchronization();

		xAchse.rotateMm(deltaX);
		yAchse.rotateMm(deltaY);

		xAchse.getMotor().endSynchronization();
		
		xAchse.getMotor().waitComplete();
		yAchse.getMotor().waitComplete();

		
		this.currentPosition = new Position3D(xAchse.getPositionFromTachoCount(), yAchse.getPositionFromTachoCount(), zAchse.isAktiv());

	}

	private void resetTachoCounts() {
		this.xAchse.resetTachoCount();
		this.yAchse.resetTachoCount();
		if (xAchse.getTachoCount() != 0 || yAchse.getTachoCount() != 0)
			throw new RuntimeException("Konnte Tachocount nicht zurücksetzen");
	}

	public void stop() {
		xAchse.stop();
		yAchse.stop();
		zAchse.stop();
	}
	/**
	 * Moves From Current Position as Current Position where position 0|0
	 * @param position2D
	 * @param mmSec
	 * @throws InterruptedException
	 */
	public void moveFromCurrentPosition(Position2D position2D, int mmSec) throws InterruptedException {
		this.moveFromCurrentPosition(new Position3D(position2D, this.zAchse.isAktiv()), mmSec);
	}
	/**
	 * Moves From Current Position as Current Position where position 0|0
	 * @param position
	 * @param speed
	 * @throws InterruptedException
	 */
	public void moveFromCurrentPosition(Position3D position, int speed) throws InterruptedException {
		Position3D CurrentPosition = this.getCurrentPosition();
		Position3D NextPosition = new Position3D(
				CurrentPosition.getX()+position.getX(),
				CurrentPosition.getY()+position.getY(),
				position.isZ());
			moveToPosition(NextPosition, speed);
		
		
	}
	/**
	 * Moves to Position 0|0
	 * @throws InterruptedException
	 */
	public void moveToZero() throws InterruptedException {
		this.moveToPosition(new Position3D(0,210,false), 100);
	}
	/**
	 * Draws an regular Polygon
	 * @param n the amount of sides of the Polygon
	 * @param widthMm the outer width of the Polygon
	 * @throws InterruptedException
	 */
	public void polygon(int n,double widthMm) throws InterruptedException {
		double Pi = Math.PI;
		double outerAngle = (2*Pi)/n;
		double innnerAngle = Pi-outerAngle;
		
		double sideLength = widthMm * Math.sin(Pi/n); //widthMm - 2*offsetMm;
		double offsetMm =  (widthMm - sideLength)/2;
		Position3D goTo = new Position3D(offsetMm,0,false);
		Position3D draw = new Position3D(0,0,true);
		
		moveFromCurrentPosition(goTo, 25);
		
		for (int i = 0; i < n ; i++) {
			double scaleX = Math.cos(outerAngle*i);
			double scaleY = Math.sin(outerAngle*i);
			draw.setX(sideLength*scaleX);
			draw.setY(-sideLength*scaleY);
			moveFromCurrentPosition(draw, 25);
			
		}
		goTo.setX(-offsetMm);
		moveFromCurrentPosition(goTo, 25);
	}
	
	/**
	 * Draws a Circle
	 * @param diameterMm
	 * @throws InterruptedException
	 */
	public void drawCircle(double diameterMm) throws InterruptedException {
		this.polygon(12, diameterMm);

//		double Circumference = diameterMm * Math.PI;
//		double speed = 20;
//		int accuracyScaler = 1000;
//		
//		this.zAchse.aktiviere();
//		Delay.msDelay(100);
//		xAchse.setSpeed(50);
//		yAchse.setSpeed(50);
//				
//		
//		xAchse.getMotor().synchronizeWith(yAchse.getMotor());
//		
//		for(double i = 1; i <= 8; i++) {
//			double xSpeed = Math.sin((2*Math.PI)*(i/(8)))*speed;
//			double ySpeed = Math.cos((2*Math.PI)*(i/(8)))*speed;
//			System.out.println("X: "+ xSpeed + " Y: "+ ySpeed + "added:" + (xSpeed+ySpeed) );
//			xAchse.setSpeed(Math.abs(xSpeed));
//			yAchse.setSpeed(Math.abs(ySpeed));
//			xAchse.getMotor().synchronizeWith(yAchse.getMotor());
//		xAchse.getMotor().startSynchronization();
//		
//		if(xSpeed > 0) {
//			xAchse.forward();
//		}
//		else if(xSpeed < 0) {
//			xAchse.backward();
//		}
//		
//		if(ySpeed > 0) {
//			yAchse.forward();
//		}
//		else if(ySpeed < 0) {
//			yAchse.backward();
//		}
//			
//		xAchse.getMotor().endSynchronization();
//		Thread.sleep(1000);
//		
//		xAchse.getMotor().startSynchronization();
//		yAchse.stop();
//		xAchse.stop();
//		xAchse.getMotor().endSynchronization();
//		}
		
		

		this.zAchse.deaktiviere();
		
	}
	
	
	

}
