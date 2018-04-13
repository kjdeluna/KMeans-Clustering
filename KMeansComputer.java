import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class KMeansComputer{
    private int k; // the number of centroids
    private LinkedList<Row> rows;
    private LinkedList<Point> centroids;
    private LinkedList<State> states;
    private final static String OUTPUT_FILENAME = "output.txt";
    public KMeansComputer(){
        this.states = new LinkedList<State>();
        this.rows = new LinkedList<Row>();
        this.centroids = new LinkedList<Point>();
    }
    public int getK(){
        return this.k;
    }
    public void reset(){
        this.states.clear();
        this.rows.clear();
        this.centroids.clear();
        this.k = -1;
    }
    public int getStateSize(){
        return this.states.size();
    }
    public void readInputFile(String inputFilename){
        try{
            FileReader fr = new FileReader(inputFilename);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] splittedData;
            // Read k
            this.k = Integer.parseInt(br.readLine());
            while((line = br.readLine()) != null){
                splittedData = line.split(" ");
                LinkedList<Double> collector = new LinkedList<Double>();
                for(String data : splittedData){
                    collector.add(Double.parseDouble(data));
                }
                this.rows.add(new Row(new Point(collector)));            }
        } catch (Exception e){}
        System.out.println(this.rows.size());
        System.out.println(this.states.size());
        System.out.println(this.centroids.size());
        // for(Row row : this.rows){
        //     for(Double val : row.getInput().getCoordinates()){
        //     }
        // }
    }
    private LinkedList<Point> randomizeCentroids(int k){
        LinkedList<Point> randomCentroids = new LinkedList<Point>();
        Set<Integer> indices = new HashSet<Integer>();
        Random r = new Random(); // instantiate randomizer object
        for(int i = 0; i < k; i++){
            int randIndex = r.nextInt(this.rows.size());
            if(!indices.contains(randIndex)){
                indices.add(randIndex);
                randomCentroids.add(this.rows.get(randIndex).getInput());
            } else { // If already containing the randIndex 
                i--; // decrement the counter to negate the increment of for loop
                continue;
            }
        }
        // for(Point point : randomCentroids){
        //     point.printCoordinates();
        // }
        return randomCentroids;
    }
    private double computeEuclideanDistance(Point x, Point y){
        double result = 0;
        LinkedList<Double> xCoordinates = x.getCoordinates();
        LinkedList<Double> yCoordinates = y.getCoordinates();
        // Compare two points together
            // Will only compute the euclidean distance if both of the points' dimensions are correct
        if(xCoordinates.size() == yCoordinates.size()){
            for(int i = 0 ; i < xCoordinates.size(); i++){
                result += Math.pow((xCoordinates.get(i) - yCoordinates.get(i)), 2);
            }
            return Math.sqrt(result);
        }
        return result;
    }
    public LinkedList<Row> getRows(){
        return this.rows;
    }
    public LinkedList<Point> getCentroids(){
        return this.centroids;
    }
    private LinkedList<Point> computeNewCentroids(){
        // Create a LinkedList of Set of Rows
        //      Set -> contains all the rows with the same classification
        // Returns null if a centroid has no associated point
        LinkedList<Set<Row>> classificationList = new LinkedList<Set<Row>>();
        LinkedList<Point> newCentroid = new LinkedList<Point>();
        // Since k == number of centroids; create k sets of classification
        for(int i = 0; i < this.k; i++){
            classificationList.add(new HashSet<Row>());
        }
        // Add rows to their corresponding classification
        for(int j = 0; j < this.rows.size(); j++){
            for(int l = 0; l < this.k; l++){
                if(this.rows.get(j).getClassification() == l + 1){
                    classificationList.get(l).add(this.rows.get(j));
                }
            }
        }
        // Actual computation of new centroids
        for(Set<Row> rowSet : classificationList){
            if(rowSet.size() == 0) return null;
            LinkedList<Double> collector = new LinkedList<Double>();
            for(int i = 0; i < this.centroids.get(0).getCoordinates().size(); i++){
                double val = 0;
                for(Row row : rowSet){
                    val += row.getInput().getCoordinates().get(i);
                }
                // Compute the average of nth coordinate of each classification
                collector.add(val / rowSet.size());
            }
            // Add the averaged coordinate to the list of centroids
            newCentroid.add(new Point(collector));
        }
        // for(Point point : newCentroid){
        //     point.printCoordinates();
        // }
        return newCentroid;
    }
    private boolean compareCentroids(LinkedList <Point> previousCentroid, LinkedList<Point> newCentroid){
        // Returns true if centroids are equal
        //      else return false
        // Compare each Point in centroid
        for(int i = 0; i < previousCentroid.size(); i++){
            // Compare each coordinate in point
            for(int j = 0; j < newCentroid.get(i).getCoordinates().size(); j++){
                int retval = Double.compare(newCentroid.get(i).getCoordinates().get(j), previousCentroid.get(i).getCoordinates().get(j));
                if(retval != 0) return false;
            }
        }
        return true;
    }
    public void setState(int k){
        this.rows = this.states.get(k).getRows();
        this.centroids = this.states.get(k).getCentroids();
    }
    public boolean runProgram(){
        int count = 0;
        // Returns true if the algorithm is already correct
        //          false if the algorithm needs to restart
        // Instantiate FileWriter
        this.centroids = this.randomizeCentroids(this.k);
        try{
            FileWriter fileWriter = new FileWriter(OUTPUT_FILENAME);
            fileWriter.write("Iteration " + count++ + ":\n");
            for(int i = 0; i < this.k; i++){
                fileWriter.write("Centroid " + (i+1) + ": (");
                for(int j = 0; j < this.centroids.get(i).getCoordinates().size(); j++){
                    fileWriter.write(Double.toString(this.centroids.get(i).getCoordinates().get(j)));
                    if(j != this.centroids.get(i).getCoordinates().size() - 1){
                        fileWriter.write(", ");
                    } else {
                        fileWriter.write(")\n");
                    }
                }
            }
            this.states.add(new State(this.rows, this.centroids));
        do{
            for(Row row : this.rows){
                row.clearRow();
                // Compute this distance of that specific row for each centroid
                for(int i = 0; i < this.k; i++){
                    row.addDistance(computeEuclideanDistance(this.centroids.get(i), row.getInput()));
                }
                // Set the classification for that specific row
                row.setClassification();
                // row.printDistance();
            }
            // Compute the new centroids
            this.states.add(new State(this.rows, this.centroids));
            LinkedList<Point> newCentroid = this.computeNewCentroids();
            // If computeNewCentroids() returns null, there is a centroid with no point associated to it;
            //          restart the algorithm
            if(newCentroid == null){
                System.out.println("Restarting algorithm");
                return false;
            }
            if(compareCentroids(this.centroids, newCentroid)){
                // Centroids have converged
                this.centroids = newCentroid;
                break;
            }
            else this.centroids = newCentroid;
            fileWriter.write("Iteration " + count++ + ":\n");
            for(int i = 0; i < this.k; i++){
                fileWriter.write("Centroid " + (i+1) + ": (");
                for(int j = 0; j < this.centroids.get(i).getCoordinates().size(); j++){
                    fileWriter.write(Double.toString(this.centroids.get(i).getCoordinates().get(j)));
                    if(j != this.centroids.get(i).getCoordinates().size() - 1){
                        fileWriter.write(", ");
                    } else {
                        fileWriter.write(")\n");
                    }
                }
            }
        } while (true);
        fileWriter.write("Iteration " + count + ":\n");
        for(int i = 0; i < this.k; i++){
            fileWriter.write("Centroid " + (i+1) + ": (");
            for(int j = 0; j < this.centroids.get(i).getCoordinates().size(); j++){
                fileWriter.write(Double.toString(this.centroids.get(i).getCoordinates().get(j)));
                if(j != this.centroids.get(i).getCoordinates().size() - 1){
                    fileWriter.write(", ");
                } else {
                    fileWriter.write(")\n");
                }
            }
        }
        this.states.add(new State(this.rows, this.centroids));
        fileWriter.close();
        }catch(Exception e){ e.printStackTrace(); }
        return true;
    }
}