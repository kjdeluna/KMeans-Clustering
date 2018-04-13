import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.Math;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;
import java.awt.geom.Line2D;
public class CoordinatePlane extends JPanel{
    private final static int MARGIN = 30;
    private static boolean drawable = false;
    private int max;
    private HashMap<String, Color> classificationColorMap;
    private int count = 0;
    private LinkedList<Row> rows;
    private LinkedList<Point> centroids;
    private static Color[] colors = {
        Color.RED,
        Color.BLUE,
        Color.MAGENTA,
        Color.ORANGE,
        Color.CYAN,
        Color.GREEN,
        Color.YELLOW,
        Color.PINK
    };

    public CoordinatePlane(){
        this.max = 0;
        this.rows = new LinkedList<Row>();
        this.centroids = new LinkedList<Point>();
        this.classificationColorMap = new HashMap<String, Color>();
        this.setLayout(null);
    }
    public void setMax(){
        LinkedList<Double> collector = new LinkedList<Double>();
        
        for(Row row : this.rows){
            for(Double d : row.getInput().getCoordinates()){
                collector.add(d);
            }
        }
        if(collector.size() != 0){
            this.max = Math.abs((int) Math.ceil((double) Collections.max(collector))) + 1;
        }
    //     for(Point point : points){
    //         LinkedList<Double> coordinates = point.getCoordinates();
    //         x.add(coordinates.get(0));
    //         y.add(coordinates.get(1));
    //     }
    //     if(x.size() != 0){
    //         maxX = (int) Math.ceil((double) Collections.max(x));
    //         minX = (int) Math.ceil((double) Collections.min(x));
    //     }
    //     if(y.size() != 0){
    //         maxY = (int) Math.ceil((double) Collections.max(y));
    //         minY = (int) Math.ceil((double) Collections.min(y));
    //     }
    }
    public void setDrawable(boolean value){
        drawable = true;
    }
    public void setRows(LinkedList<Row> rows){
        this.rows = rows;
    }
    public void setCentroids(LinkedList<Point> centroids){
        this.centroids = centroids;
    }
    // public void setMax(int max){
    //     this.max = max;
    // }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int drawingComponentWidth = this.getWidth();
        int drawingComponentHeight = this.getHeight();
        // Drawing Cartesian Plane
        g2d.draw(new Line2D.Double(this.getWidth() / 2, 0,  this.getWidth()/2, this.getHeight()));
        g2d.draw(new Line2D.Double(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2 ));
        if(drawable){
            setMax();
            int gap = (int) (drawingComponentWidth / 2) / max;
            for(Row row : this.rows){
                g2d.setColor(dynamicChangeColor(Integer.toString(row.getClassification())));
                g2d.fillOval((int) (row.getInput().getCoordinates().get(0) * gap + this.getWidth() / 2) - 2, (int) (this.getHeight() / 2 - row.getInput().getCoordinates().get(1) * gap), 5 ,5);
            }
            for(int i = 0; i < this.centroids.size(); i++){
                double xStart = this.centroids.get(i).getCoordinates().get(0) * (gap) + this.getWidth() / 2;
                double yStart = this.getHeight() / 2 - this.centroids.get(i).getCoordinates().get(1) * (gap);
                int x[] = {(int) xStart - 3, (int) xStart, (int) xStart + 3};
                int y[] = {(int) yStart, (int) yStart + 5, (int) yStart};
                g2d.setColor(dynamicChangeColor(Integer.toString(i + 1)));
                g2d.drawPolygon(x, y, 3);
            }
            g2d.setColor(Color.BLACK);
            // Drawing y-axis anchors
            for(int i = max * 2; i > -(max * 2); i--){
                if(i == 0) continue;
                g2d.drawLine((int) this.getWidth() / 2 - MARGIN / 4, (i * gap) + 5 , MARGIN / 4 + this.getWidth() / 2, i  * gap + 5);
                g2d.drawString(Integer.toString(i), (int) this.getWidth() / 2 + 12, (int) gap * (max - i) + 9);
            }
            // Drawing x-axis anchors
            for(int i = max * 2; i > -(max * 2); i--){
                if(i == 0) continue;
                g2d.drawLine((int) (i * gap) + 5, this.getHeight() / 2 - MARGIN/4, (int) (i * gap) + 5, this.getHeight() / 2 + MARGIN/4);
                g2d.drawString(Integer.toString(-i), (int) gap * (max - i), (int) this.getHeight() / 2 + MARGIN);
            }
        }
    }

    private Color dynamicChangeColor(String classification){
        if(!classificationColorMap.containsKey(classification)){
            classificationColorMap.put(classification,colors[count++]);
            return colors[count-1];
        } else {
            return classificationColorMap.get(classification);
        }
    }
}