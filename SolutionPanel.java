import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JViewport;
import java.util.LinkedList;

public class SolutionPanel extends JPanel{
    private JTable classCentroidTable;
    private JScrollPane classCentroidTableScrollPane;
    private CoordinatePlane plane;
    private JFileChooser inputFileChooser;
    private JButton prevButton;
    private JButton nextButton;
    private JButton selectInputFileButton;
    private static int counter;
    private KMeansComputer km;
    public SolutionPanel(){
        counter = 0;
        this.km = new KMeansComputer();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Initializing inputFileChooser
        this.inputFileChooser = new JFileChooser();
        this.inputFileChooser.setCurrentDirectory(new File("."));
        this.inputFileChooser.setDialogTitle("Open training_data File");

        // Place selectInputFileButton
        this.selectInputFileButton = new JButton("Select input.txt");
        this.selectInputFileButton.setFocusable(false);
        c.ipady = 10;
        c.ipadx = 0;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weightx = 0;
        c.insets = new Insets(20, 10, 10, 10);
        c.anchor = GridBagConstraints.NORTHWEST;
        this.add(this.selectInputFileButton, c);

        // Initializing and placing coordinate plane
        this.plane = new CoordinatePlane();
        plane.setBackground(new Color(192,192,192));
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 801;
        c.ipadx = 801;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 10, 10, 10);
        this.add(this.plane, c);
        // Initializing and placing inputClassTable
		String[] columnNames = {
			"Class",
			"Centroid" 
		};
        Object[][] rowData = new Object[0][0];
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        this.classCentroidTable = new JTable(model);
        this.classCentroidTableScrollPane = new JScrollPane(this.classCentroidTable);
        this.classCentroidTableScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        c.ipady = 20;
        c.ipadx = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.classCentroidTableScrollPane, c);

        // Initializing prev and next buttons
        this.prevButton = new JButton("Previous");
        this.prevButton.setEnabled(false);
        c.ipady = 15;
        c.ipadx = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(30,10,0,0);
        this.add(this.prevButton, c);

        this.nextButton = new JButton("Next");
        this.nextButton.setEnabled(false);
        c.ipady = 15;
        c.ipadx = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(30,0,0,10);
        this.add(this.nextButton, c);


        // Adding anonymous action listeners to buttons
        this.selectInputFileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                    km.reset();
                    km.readInputFile(inputFileChooser.getSelectedFile().toString());
                    nextButton.setEnabled(true);
                    km.runProgram();
                    km.setState(0);
                    putDataToInputClassTable();
                    if(km.getCentroids().get(0).getCoordinates().size() == 2) plane.setDrawable(true);
                    System.out.println(km.getRows());
                    plane.setRows(km.getRows());
                    plane.setCentroids(km.getCentroids());
                    plane.repaint();
                }
            }
        });

        this.nextButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(counter != km.getStateSize() -1){
                    km.setState(++counter);
                    putDataToInputClassTable();
                    nextButton.setEnabled(true);
                    prevButton.setEnabled(true);
                }
                if(counter == km.getStateSize() - 1) nextButton.setEnabled(false);
                if(km.getCentroids().get(0).getCoordinates().size() == 2){
                    plane.setDrawable(true);
                    plane.setRows(km.getRows());
                    plane.setCentroids(km.getCentroids());
                    plane.repaint();
                }

            }
        });

        this.prevButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(counter != 0){
                    km.setState(--counter);
                    putDataToInputClassTable();
                    prevButton.setEnabled(true);
                    nextButton.setEnabled(true);
                }
                if(counter == 0) prevButton.setEnabled(false);
                System.out.println("print: " + km.getCentroids().get(0).getCoordinates().size());
                if(km.getCentroids().get(0).getCoordinates().size() == 2){
                    plane.setDrawable(true);
                    plane.setRows(km.getRows());
                    plane.setCentroids(km.getCentroids());
                    plane.repaint();
                }
            }
        });
    }

    private void putDataToInputClassTable(){
        DefaultTableModel tableModel = (DefaultTableModel) classCentroidTable.getModel();
        tableModel.setRowCount(0);
        for(int i = 0; i < this.km.getK(); i++){
            String[] data = new String[2];
            if(counter != 0) data[0] = "C" + (i + 1);
            else data[0] = "undetermined";
            data[1] = "(";
            for(int j = 0; j < this.km.getCentroids().get(0).getCoordinates().size(); j++){
                data[1] += Double.toString(this.km.getCentroids().get(i).getCoordinates().get(j));
                if(j != this.km.getCentroids().get(0).getCoordinates().size() - 1){
                    data[1] += ", ";
                } else data[1] += ")";
            }
            tableModel.addRow(data);
        }
        this.classCentroidTable.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    }
}
