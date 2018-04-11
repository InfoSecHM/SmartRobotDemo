package internes;


import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class RPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final Font labelFont = new Font("Comic", Font.BOLD, 16);
	public static final int LABEL_HEIGHT = 30;
	private JPanel pnlPrivate;
	public JPanel pnlPublic;

	public RPanel(double width, double height, String label) {
		pnlPrivate = new JPanel(null);
		pnlPrivate.setBounds(0, 0, (int) width, LABEL_HEIGHT);
		pnlPrivate.add(UIUtils.createLabel((int) width, LABEL_HEIGHT, label));

		pnlPublic = new JPanel(null);
		pnlPublic.setBounds(0, LABEL_HEIGHT, (int) width, (int) height - LABEL_HEIGHT);

		pnlPublic.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// pnlPublic.setBorder(BorderFactory.createTitledBorder("abc"));
		// pnlPublic.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		super.setLayout(null);
		super.setBounds(0, 0, (int) width, (int) height);

		super.add(pnlPrivate);
		super.add(pnlPublic);
	}

	@Override
	public Component add(Component comp) {
		return pnlPublic.add(comp);
	}

	@Override
	public void add(Component comp, Object border) {
		pnlPublic.add(comp, border);
	}

	public void setLayoutMgr(LayoutManager mgr) {
		pnlPublic.setLayout(mgr);
	}

	@Override
	public void removeAll() {
		pnlPublic.removeAll();
	}

	@Override
	public void doLayout() {
		pnlPublic.doLayout();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();

		RPanel pnl = new RPanel(300, 300, "Robot World");
		frame.add(pnl);

		frame.setSize(width, height);
		frame.setVisible(true);
	}

}
