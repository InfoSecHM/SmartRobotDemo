package filters.histogram;


import internes.RPanel;
import internes.RacineView;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class HistogramFilterAdvView extends RacineView {

	private static final long serialVersionUID = 1L;

	final int[][] world = new int[][] { { 1, 0, 0, 0 }, { 0, 0, 0, 0 },
			{ 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 }, };;
	HistogramFilter filter = null;

	// Robot Motions Controls
	JSpinner spnMotionNoise;
	SpinnerNumberModel spnMotionNoiseModal = new SpinnerNumberModel(0, 0, 1,
			0.01);
	JCheckBox chkCyclic;
	public final static String[] btnNames = { "Up-Left", "Up", "Up-Right",
			"Left", "No_Move", "Right", "Down-Left", "Down", "Down-Right" };
	JButton[] btnMotions = { new JButton(""), new JButton(""), new JButton(""),
			new JButton(""), new JButton(""), new JButton(""), new JButton(""),
			new JButton(""), new JButton("") };

	// Robot Sensor Controls
	JSpinner spnSensorNoise;
	SpinnerNumberModel spnSensorNoiseModal = new SpinnerNumberModel(0, 0, 1,
			0.01);
	JButton[] btnSensors = { new JButton("Sense") };
	JButton btnReset;
	JButton btnApply;
	JButton btnWorldConfiguration;
	JButton btnResetSimulation;

	static double DEFAULT_SENSOR_NOISE = 0.20;
	static double DEFAULT_MOTION_NOISE = 0.20;
	static boolean DEFAULT_CYCLIC_WORLD = false;

	static int PANEL_WIDTH = 300;
	static int PANEL_HEIGHT = 300;

	RPanel pnlRobotMotions;
	RPanel pnlRobotSettings;
	RPanel pnlBeliefMap;

	public HistogramFilterAdvView() {
		super("Histogram Filter (Markov Localization)",
				"config/Histogram.properties");
		setLayout(null);
		loadProperties();

		setSize(screenSize);
		setVisible(false);
	}

	public void initGUI() {
		isInit = true;
		// Set this world to Localizer
		filter = new HistogramFilter(world);
		// Setting the default noise
		filter.setMotionNoise(DEFAULT_MOTION_NOISE);
		filter.setSensorNoise(DEFAULT_SENSOR_NOISE);
		filter.setCyclic(DEFAULT_CYCLIC_WORLD);

		// //////////// Setting Panel

		pnlRobotSettings = new RPanel(PANEL_WIDTH, PANEL_HEIGHT,
				"Robot Setting");
		createSensorsComponents(pnlRobotSettings);

		// ////////// Robot Belief Map
		pnlBeliefMap = new RobotBeliefMap(this, PANEL_WIDTH * 2,
				PANEL_HEIGHT * 2, "Robot Belief Map");
		pnlBeliefMap.setLocation(PANEL_WIDTH, 0);
		add(pnlBeliefMap);

		// ////////// Controls Panel

		pnlRobotMotions = new RPanel(PANEL_WIDTH, PANEL_HEIGHT,
				"Motion Controls");
		pnlRobotMotions.setLocation(0, PANEL_HEIGHT);
		createMotionComponents();

		// Add panels to frame
		getContentPane().add(pnlRobotMotions);
		getContentPane().add(pnlRobotSettings);

	}

	private void createMotionComponents() {
		// Add Motion buttons
		pnlRobotMotions.setLayoutMgr(new GridLayout(3, 3, 5, 5));

		RobotMotionListener motionList = new RobotMotionListener();
		for (int i = 0; i < btnMotions.length; i++) {
			btnMotions[i].setActionCommand(String.valueOf(i));
			btnMotions[i].setIcon(new ImageIcon(getClass().getResource(
					"/images" + File.separatorChar + btnNames[i] + ".png")));
			btnMotions[i].setToolTipText(btnNames[i]);
			pnlRobotMotions.add(btnMotions[i]);
			btnMotions[i].addActionListener(motionList);
		}

	}

	private void createSensorsComponents(JPanel pnlRobotSetting) {
		int spacing = 3;

		int xLoc = 0;
		int yLoc = 0;
		int width = PANEL_WIDTH / 2 - 1;
		int height = Math.min(PANEL_HEIGHT / 8, 30);
		JLabel lblMotion = new JLabel("Motion Noise");
		lblMotion.setBounds(xLoc + spacing, yLoc + spacing, width - spacing,
				height - spacing);
		pnlRobotSetting.add(lblMotion);

		spnMotionNoise = new JSpinner();
		spnMotionNoise.setBounds(xLoc + width + spacing, yLoc + spacing, width
				- spacing, height - spacing);
		spnMotionNoise
				.setToolTipText("Set the ROBOT motion noise.It should be (0-1)");
		spnMotionNoise.setModel(spnMotionNoiseModal);
		spnMotionNoise.setValue(DEFAULT_MOTION_NOISE);
		pnlRobotSetting.add(spnMotionNoise);

		lblMotion = new JLabel("Cyclic World");
		yLoc += height;
		lblMotion.setBounds(xLoc + spacing, yLoc + spacing, width - spacing,
				height - spacing);
		pnlRobotSetting.add(lblMotion);

		chkCyclic = new JCheckBox("");
		chkCyclic.setBounds(xLoc + width + spacing, yLoc + spacing, width
				- spacing, height - spacing);
		chkCyclic.setToolTipText("UnCheck it if the ROBOT world is not cyclic");
		chkCyclic.setSelected(DEFAULT_CYCLIC_WORLD);
		pnlRobotSetting.add(chkCyclic);

		lblMotion = new JLabel("Sensor Noise");
		yLoc += height;
		lblMotion.setBounds(xLoc + spacing, yLoc + spacing, width - spacing,
				height - spacing);
		pnlRobotSetting.add(lblMotion);

		spnSensorNoise = new JSpinner();
		spnSensorNoise.setBounds(xLoc + width + spacing, yLoc + spacing, width
				- spacing, height - spacing);
		spnSensorNoise
				.setToolTipText("Set the ROBOT sensor noise.It should be (0-1)");
		spnSensorNoise.setModel(spnSensorNoiseModal);
		spnSensorNoise.setValue(DEFAULT_SENSOR_NOISE);
		pnlRobotSetting.add(spnSensorNoise);

		btnApply = new JButton("Apply Setting");
		yLoc += height;
		btnApply.setBounds(xLoc + spacing, yLoc + spacing, width - spacing,
				height - spacing);
		pnlRobotSetting.add(btnApply);
		RobotControlListener controlListener = new RobotControlListener();
		btnApply.addActionListener(controlListener);

		btnReset = new JButton("Reset Belief");
		btnReset.setBounds(xLoc + width + spacing, yLoc + spacing, width
				- spacing, height - spacing);
		pnlRobotSetting.add(btnReset);
		btnReset.addActionListener(controlListener);

		btnWorldConfiguration = new JButton("Configure World");
		yLoc += height;
		btnWorldConfiguration.setBounds(xLoc + spacing, yLoc + spacing, width
				- spacing, height - spacing);
		pnlRobotSetting.add(btnWorldConfiguration);
		btnWorldConfiguration.addActionListener(controlListener);

		// //////////////////////////////////////////
		JLabel header = UIUtils.createLabel(PANEL_WIDTH, LABEL_HEIGHT,
				"Robot Sensors");
		yLoc += height;
		header.setBounds(xLoc + spacing, yLoc + spacing, 2 * width - spacing,
				height - spacing);
		pnlRobotSetting.add(header);

		yLoc += height;
		JPanel pnlSouth = new JPanel(new GridLayout(1, 5, 10, 10));
		pnlSouth.setBounds(xLoc + spacing, yLoc + spacing, 2 * width - spacing,
				height - spacing);
		RobotSensorListener sensorListener = new RobotSensorListener();
		for (int i = 0; i < btnSensors.length; i++) {
			btnSensors[i].setActionCommand(Integer.toString(i));
			btnSensors[i].addActionListener(sensorListener);
			btnSensors[i].setMnemonic(btnSensors[i].getText().charAt(0));
			pnlSouth.add(btnSensors[i]);
		}
		pnlRobotSetting.add(pnlSouth);
	}

	private class RobotControlListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JButton o = (JButton) e.getSource();
			if (o.equals(btnReset)) {
				filter.resetBelief();
				pnlBeliefMap.repaint();
			} else if (o.equals(btnApply)) {
				filter.setMotionNoise(Double.parseDouble(spnMotionNoise
						.getValue().toString()));
				filter.setSensorNoise(Double.parseDouble(spnSensorNoise
						.getValue().toString()));
				filter.setCyclic(chkCyclic.isSelected());

				filter.setWorld(world);
				pnlBeliefMap.repaint();
			}
		}

	}

	private class RobotMotionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			try {
				int actionIdx = Integer.parseInt(action);
				filter.move(actionIdx);
				pnlBeliefMap.repaint();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	private class RobotSensorListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			try {
				int actionIdx = Integer.parseInt(action);
				filter.sense(actionIdx);
				pnlBeliefMap.repaint();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean loadProperties() {

		PANEL_HEIGHT = (int) (screenSize.getHeight() / 2 - 80);
		PANEL_WIDTH = PANEL_HEIGHT - LABEL_HEIGHT;

		System.out.println("Property File = " + propertyFile);

		return true;
	}

	public void saveProperties() {

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JDesktopPane desk = new JDesktopPane();
		frame.setContentPane(desk);

		HistogramFilterAdvView view1 = new HistogramFilterAdvView();
		view1.initGUI();

		desk.add(view1);
		view1.setVisible(true);

		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();

		frame.setSize(width, height);
		frame.setVisible(true);
	}

}
