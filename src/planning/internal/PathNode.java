package planning.internal;


import utils.RoboMathUtils;

/*
 * This is used to represent path location for path from start to goal.
 * This contains minimum information required to represent path node.
 */
public class PathNode implements Cloneable {
	private double x;
	private double y;

	public PathNode(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public PathNode(PathNode node) {
		this.x = node.getX();
		this.y = node.getY();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
		    return false;
		PathNode that = (PathNode) obj;
		if (this.getX() == that.getX() && this.getY() == that.getY())
			return true;
		return false;
	}

	@Override
	public PathNode clone() throws CloneNotSupportedException {
		return (PathNode) super.clone();
	}

	@Override
	public String toString() {
		return "Path [x=" + RoboMathUtils.round(x, 3) + ", y=" + RoboMathUtils.round(y, 3) + "]";
	}
}
