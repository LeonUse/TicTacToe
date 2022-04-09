package plott3r_V1.geometrie;

import java.util.ArrayList;
import java.util.List;

import plott3r_V1_solved.Roboter;
import positions.Position2D;
import positions.Position3D;

public class X extends GeometrischeFigur {

	public X(Position2D mittelpunkt, int radius) {
		super(mittelpunkt, radius);
	}

	@Override
	protected List<Position2D> calculatePositions() {
		List<Position2D> ret = new ArrayList<>();
        ret.add(new Position2D(this.getMittelpunkt().getX(), this.getMittelpunkt().getY() + this.getRadius()));
        ret.add(new Position2D(this.getMittelpunkt().getX(), this.getMittelpunkt().getY() - this.getRadius()));
        return ret;
	}

	public void zeichneX(Roboter roboter) {
    	try {
    		roboter.moveToPosition(new Position3D(getMittelpunkt().getX()- (getRadius() / 2),getMittelpunkt().getY()-(getRadius() / 2), false), 30);
			roboter.moveFromCurrentPosition(new Position3D(getRadius(),getRadius(),true), 30);
			roboter.moveFromCurrentPosition(new Position3D(getRadius() * -1,0,false), 30);
			roboter.moveFromCurrentPosition(new Position3D(getRadius(),getRadius() * -1,true), 30);
			roboter.moveToZero();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
