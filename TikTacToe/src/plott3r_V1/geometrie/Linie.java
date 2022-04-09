package plott3r_V1.geometrie;

import java.util.ArrayList;
import java.util.List;

import plott3r_V1_solved.Roboter;
import positions.Position2D;
import positions.Position3D;

public class Linie extends GeometrischeFigur {

    public Linie(Position2D mittelpunkt, int radius) {
        super(mittelpunkt, radius);
    }

    public Linie(Position2D mittelpunkt, int radius, int rotation) {
        super(mittelpunkt, radius, rotation);
    }
    
    public void zeichneLinie(Roboter roboter) {
    	try {
    		roboter.moveToPosition(new Position3D(getMittelpunkt().getX(),getMittelpunkt().getY()-(getRadius() / 2), false), 30);
			roboter.moveFromCurrentPosition(new Position3D(0,getRadius(),true), 30);
			roboter.moveToZero();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected List<Position2D> calculatePositions() {
        List<Position2D> ret = new ArrayList<>();
        ret.add(new Position2D(this.getMittelpunkt().getX(), this.getMittelpunkt().getY() + this.getRadius()));
        ret.add(new Position2D(this.getMittelpunkt().getX(), this.getMittelpunkt().getY() - this.getRadius()));
        return ret;
    }

}
