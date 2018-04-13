import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
public class Main{
    public static final int WIDTH = 1020;
    public static final int HEIGHT = 720;
    private static final String TITLE = "K Means Clustering";

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
 
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private static void addComponentsToPane(Container pane) {
        SolutionPanel sp = new SolutionPanel();
        pane.add(sp);
    }
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        // KMeansComputer km = new KMeansComputer("input.txt");
        // while(!km.runProgram());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}