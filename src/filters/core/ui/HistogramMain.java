package filters.core.ui;


import javax.swing.*;
import java.awt.*;
import java.util.List;

import filters.core.*;
import filters.core.objects.GridRobotBelief;
import filters.sensors.RangeSensor;
import filters.sensors.RobotDirection;
import filters.sensors.SonarRangeModule;
import internes.RacineView;

public class HistogramMain extends RacineView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static GridWorldExplorer exp;

	public HistogramMain() {
		super("Histogram Filter(Sonar Range Finder)", "");
	}

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JDesktopPane desk = new JDesktopPane();
		frame.setContentPane(desk);

		HistogramMain view1 = new HistogramMain();
		view1.initGUI();

		desk.add(view1);
		view1.setVisible(true);

		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();

		frame.setSize(width, height);
		frame.setVisible(true);

	}

	@Override
	public void initGUI() {
		GridWorldDomain domain = new GridWorldDomain(11, 11);
		domain.initDefaultWorld();

		Domain d = domain.generateDomain();

		// Setup Noise
		double motionNoise = 0.1;
		double sensorNoise = 0.1;

		// setup initial state
		State s = domain.getOneRobotBeliefState(d);
		domain.setAgent(s, 4, 2, 0);
		domain.setCyclicWorld(false);
		domain.initUniformBelief(s);
		domain.initializeSensors(s, d, motionNoise, sensorNoise);

		Visualizer v = GridWorldVisualizer.getVisualizer(domain.getMap());
		v.updateState(s);

		exp = new GridWorldExplorer(this, d, v, s);

		// TODO
		List<KeyActionBinding> act = domain.getKeyActionsBindings();
		for (KeyActionBinding action : act) {
			exp.addKeyAction(action.getKey(), action.getActionName());
		}
		// set control keys to use w-s-a-d

		exp.addKeyAction("l", GridWorldDomain.ACTION_SENSE);

		exp.initGUI();

		// super.add(v);

		setSize(screenSize.getWidth(), screenSize.getHeight() - 60);
		setVisible(true);
	}
}
