package filters.sensors;


import filters.core.State;

public interface RangeSensor {

	int getMaxRange();

	void setMaxRange(int maxRange);

	double getNoise();

	void setNoise(double noise);

	int getMeasurement();

	void setMeasurement(int measurement);

	RobotDirection getDirection();

	void sense(State s, int cx, int cy, int theta, int[][] map);

}
