package main;

import internes.RacineView;
import planning.pathsmoother.views.PathSmoothingView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

public class Main extends JFrame implements ActionListener {

    private final static String APPLICATION_TITLE = "SMART EISTI";
    final static String version = " 1.0";
    private static final long serialVersionUID = 1L;

    // pour creer le panel de l'application qui va contenir les frames
    private JDesktopPane desk;

    JMenuItem miExit;
    JMenuItem miAbout;

    public Main(String title) {
        //invoquer les constructeur de la classe parente
        super(title);
        //quitter le frame en appuyant sur le bouton close
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //maximiser la longeueur/largeur du frame
        setExtendedState(Frame.MAXIMIZED_BOTH);
        String os = System.getProperty("os.name");
        try {
            if (os != null && os.contains("Windows")) {

            } else {
                //"Look" se réfère à l'apparence des widgets GUI (plus formellement, JComponents) et "feel" se réfère à la façon dont les widgets se comportent.
                //CrossPlatformLookAndFeel - c'est le "Java L & F" (aussi appelé "Metal") qui a la même apparence sur toutes les plateformes. Il fait partie de l'API Java (javax.swing.plaf.metal) et est la valeur par défaut qui sera utilisée si vous ne faites rien dans votre code pour définir un L & F différent.
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Créez un bureau et définissez-le en tant que volet de contenu
        desk = new JDesktopPane();
        setContentPane(desk);
        // Installer notre gestionnaire de bureau personnalisé

        desk.setDesktopManager(new BureauManager());
        creationMenu();
        chargerimage();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                    JInternalFrame[] frms = desk.getAllFrames();
                    for (int i = 0; i < frms.length; i++) {
                        try {
                            if (frms[i] instanceof RacineView) {
                                ((RacineView) frms[i]).setClosed(true);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }
        });


    }

    protected void chargerimage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/rsz_plafond.jpg"));
        JLabel l = new JLabel(icon);
        l.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

        // Placer l'image dans la couche la plus basse possible pour que rien
        // peut jamais être peint dessous.
        desk.add(l, new Integer(Integer.MIN_VALUE));
    }

    //ajouter un noveau JInteralFrame au demande
    class AjouterFrameAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        JInternalFrame frame = null;

        public AjouterFrameAction(String name, JInternalFrame frame) {
            super(name);
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            RacineView view = (RacineView) frame;
            view.setVisible(true);
            try {
                view.setSelected(true);
                if (!view.isInit)
                    view.initGUI();
            } catch (PropertyVetoException e) {
            }
        }


    }

    protected void creationMenu() {
        JMenuBar mb = new JMenuBar();

        mb.setBackground(Color.red);
        JMenu mnFile = new JMenu("Fichier");

        mnFile.add(new InitialisationBureau(desk));

        miExit = new JMenuItem("Quitter");
        miExit.addActionListener(this);
        mnFile.add(miExit);
        JMenu mnLocalisation = new JMenu("Affichage");

       /* JInternalFrame histogramFilter = new HistogramFilterView();
        mnLocalization.add(new AddFrameAction("Histogram Filter(Color Sensor)", histogramFilter));
        desk.add(histogramFilter);

        JInternalFrame histogramFilter2 = new HistogramMain();
        mnLocalization.add(new AddFrameAction("Histogram Filter(Laser Sensor)", histogramFilter2));
        desk.add(histogramFilter2);

        JInternalFrame kalmanFilter = new KalmanFilterView();
        mnLocalization.add(new AddFrameAction("Kalman Filter", kalmanFilter));
        desk.add(kalmanFilter);

        JInternalFrame particleFilter = new ParticleFilterView();
        mnLocalization.add(new AddFrameAction("Particle Filter", particleFilter));
        desk.add(particleFilter);

        JMenu mnPlanning = new JMenu("Plan");

        JInternalFrame pathPlanning = new PathPlannerView();
        mnPlanning.add(new AddFrameAction("Path Planning", pathPlanning));
        desk.add(pathPlanning);




        JMenu mnControl = new JMenu("Controller");
        JInternalFrame pidController = new PIDControllerView();
        mnControl.add(new AddFrameAction("PID Controller", pidController));
        desk.add(pidController); */

        JMenu mnOptimization = new JMenu("Optimisation");
        JInternalFrame chemin = new PathSmoothingView();
        mnOptimization.add(new AjouterFrameAction("Chemin", chemin));
        desk.add(chemin);


        JMenu mnHelp = new JMenu("Help");

        miAbout = new JMenuItem("About");
        miAbout.addActionListener(this);
        mnHelp.add(miAbout);


        setJMenuBar(mb);
       // mb.add(mnFile);
        mb.add(mnLocalisation);
       // mb.add(mnPlanning);
        mb.add(mnOptimization);
        //mb.add(mnControl);
        mb.add(mnHelp);


    }


    public static void main(String[] args) {

        Main lancer_programme = new Main(APPLICATION_TITLE + version);

        Toolkit tool = Toolkit.getDefaultToolkit();
        lancer_programme.setSize(tool.getScreenSize());
        lancer_programme.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JMenuItem) {
            if (obj.equals(miExit)) {
                System.exit(0);
            } else if (obj.equals(miAbout)) {
                afficherdetails();

            }
        }

    }
    private void afficherdetails() {
        Icon ico = new ImageIcon(getClass().getResource("/images/rsz_nous.jpg"));
        JOptionPane.showOptionDialog(null, "Smart EISTI \n Version = " + version
                        + "\n Développé par : \n Mahdi HRAYBI\n Julien Chiang\n Antoine DENIS\n Alexy DALEY\n", "Qui sommes nous", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, ico, new Object[] {}, null);

    }
}
    class InitialisationBureau extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private JDesktopPane desk; // le bureau ou on travaille

        public InitialisationBureau(JDesktopPane desk) {
            super("Voir tous les frames");
            this.desk = desk;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        // le code est valable sur google et surtout sur stackoverflow pour regler les frames et la grille

            //chercher combien de frames on a
            JInternalFrame[] allframes = desk.getAllFrames();
            int count = allframes.length;
            if (count == 0)
                return;

            //Déterminer la taille de la grille nécessaire
            int sqrt = (int) Math.sqrt(count);
            int rows = sqrt;
            int cols = sqrt;
            if (rows * cols < count) {
                cols++;
                if (rows * cols < count) {
                    rows++;
                }
            }

            // Définit des valeurs initiales pour la taille et l'emplacement.
            Dimension size = desk.getSize();

            int w = size.width / cols;
            int h = size.height / rows;
            int x = 0;
            int y = 0;

            // Itérer sur les frames, en désiconifiant les frames iconisés, puis
            // relocaliser et redimensionner chacune
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols && ((i * cols) + j < count); j++) {
                    JInternalFrame f = allframes[(i * cols) + j];

                    if (!f.isClosed() && f.isIcon()) {
                        try {
                            f.setIcon(false);
                        } catch (PropertyVetoException ignored) {
                        }
                    }

                    desk.getDesktopManager().resizeFrame(f, x, y, w, h);
                    x += w;
                }
                y += h; // commencer la 2eme ligne
                x = 0;
            }
        }
        }



class BureauManager extends DefaultDesktopManager {

    private static final long serialVersionUID = 1L;

    //Ceci est appelé à chaque fois qu'un cadre est déplacé. Cette
//implémentation empêche le cadre de quitter le bureau.

    @Override
    public void dragFrame(JComponent f, int x, int y) {

        if (f instanceof JInternalFrame) { // Traiter uniquement les cadres internes
            JInternalFrame frame = (JInternalFrame) f;
            JDesktopPane desk = frame.getDesktopPane();
            Dimension d = desk.getSize();

            // pour conserver le cadre sur le bureau.
            if (x < 0) { // trop loin à gauche?
                x = 0; // affleurer contre le côté gauche
            } else {
                if (x + frame.getWidth() > d.width) { // trop loin à droite ?
                    x = d.width - frame.getWidth(); // affleurer contre le côté droit
                }
            }
            if (y < 0) { // trop haut?
                y = 0; // affleurer contre le haut
            } else {
                if (y + frame.getHeight() > d.height) { // trop bas?
                    y = d.height - frame.getHeight(); // affleurer contre le bas
                }
            }
        }

        // Transmet les valeurs (éventuellement recadrées) au gestionnaire de glissement normal.
        super.dragFrame(f, x, y);
    }
}

