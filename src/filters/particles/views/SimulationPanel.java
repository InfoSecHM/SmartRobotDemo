package filters.particles.views;



import filters.particles.ParticleSimulator;
import filters.particles.World;
import filters.particles.internal.SimulationObject;

import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ParticleSimulator simulation;
	public JPanel canvas;

	public SimulationPanel(ParticleSimulator s) {
		this.simulation = s;
		setLayout(null);
		canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(0, 0, World.getWidth() + World.getWallSize(), World.getHeight() + World.getWallSize());
		add(canvas);
	}

	@Override
	public void paint(Graphics g) {
		canvas.repaint();
	}

	private class Canvas extends JPanel {

		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			for (SimulationObject o : simulation) {
				o.onPaint(g2d);
			}

			g.setColor(Color.MAGENTA);
			g.fillRect(0, 0, World.getWidth(), World.getWallSize());// top
			g.fillRect(0, 0, World.getWallSize(), World.getHeight());// left
			g.fillRect(World.getWidth() - World.getWallSize(), 0, World.getWallSize(), World.getHeight());// right
			g.fillRect(0, World.getHeight() - World.getWallSize(), World.getWidth(), World.getWallSize());// bottom

		}
	}
}
