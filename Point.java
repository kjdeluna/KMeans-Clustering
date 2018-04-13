import java.util.LinkedList;

public class Point{
    private LinkedList<Double> coordinates;
    private int classification;
    public Point(LinkedList<Double> coordinates){
        this.coordinates = coordinates;
    }

    public LinkedList<Double> getCoordinates(){
        return this.coordinates;
    }

    public int getClassification(){
        return this.classification;
    }
    public void printCoordinates(){
        for(double val : this.coordinates){
            System.out.print(val + " ");
        }
        System.out.println("");
    }
}