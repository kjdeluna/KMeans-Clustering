import java.util.LinkedList;

public class State{
    private LinkedList<Row> rows;
    private LinkedList<Point> centroids;
    public State(LinkedList<Row> rows, LinkedList<Point> centroids){
        // this.rows = new LinkedList<Row>(rows);
        LinkedList<Row> delta = new LinkedList<Row>();
        for(Row row : rows){
            delta.add(new Row(row.getInput(), row.getClassification()));
        }
        this.rows = delta;
        this.centroids = new LinkedList<Point>(centroids);
    }
    public LinkedList<Row> getRows(){
        return this.rows;
    }
    public LinkedList<Point> getCentroids(){
        return this.centroids;
    }
}