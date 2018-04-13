import java.util.LinkedList;

public class Row{
    private Point input;
    private LinkedList<Double> distances;   
    private int classification;
    public Row(Point input){
        this.input = input;
        this.distances = new LinkedList<Double>();
        this.classification = -1;
    }
    public Row(Point input, int classification){
        this.input = input;
        this.distances = new LinkedList<Double>();
        this.classification = classification;
    }
    public Point getInput(){
        return this.input;
    }
    public void addDistance(Double val){
        this.distances.add(val);
    }
    public void clearRow(){
        this.distances.clear();
        this.classification = -1;
    }
    public void printDistance(){
        for(Double d : input.getCoordinates()){
            System.out.print(d + " ");
        }
        for(Double d : distances){
            System.out.print(d + " ");
        }
        System.out.println(this.classification);
        System.out.println("");
    }
    public int getClassification(){
        return this.classification;
    }
    public void setClassification(){
        int minIndex = - 1;
        double minDistance = -1;
        for(int i = 0; i < this.distances.size(); i++){
            if(i == 0){
                minDistance = this.distances.get(i);
                minIndex = 0;
            }
            else if(this.distances.get(i) < minDistance){
                minDistance = this.distances.get(i);
                minIndex = i;
            }
        }
        this.classification = minIndex + 1;
    }
}