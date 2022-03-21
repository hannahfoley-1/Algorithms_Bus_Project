import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    final double infinity = Double.POSITIVE_INFINITY;
    boolean valid = true;
    BusStopsList allBusStops;
    //integer represents the node that the edge is coming from, the edge list is the egdes that can be reached from the first node
    public static HashMap<Integer, ArrayList<Integer>> destinations = new HashMap<>();
    public static HashMap<Integer, List<EdgeJourney>> adjacencyList = new HashMap<>();
    public static HashMap<Integer, Integer> transferType = new HashMap<>();
    //this matrix represents the distance between two nodes i and j. The distance is stored in matrix[i][j]
    //Time[][] minTransferTimeMatrix = new Time[allBusStops.size()][allBusStops.size()];

    Graph(String filename, BusStopsList allBusStops) throws IOException {//if the file name is null, we cannot read from the file and so the graph cannot be made
        if (filename == null || filename == "") {
            valid = false;
        }

        this.allBusStops = allBusStops;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line = reader.readLine();
            line = reader.readLine();
            //the matrix is made up of an integer array which contains two integers : the source and the destination intersections
            // matrix [source][destination] will give the distance between the source and the destination node

            //the matrix isn't completely space efficient but searching using the matrix is very efficient

            //initialise all of matrix elements as positive infinity
            //go through matrix and make empty spaces infinity

            int count = 0;
            while (line != null && valid && count < 5084) {
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                count ++;
                System.out.println(count);
                ArrayList<Integer> destinationsFrom;
                if(destinations == null)
                {
                    destinationsFrom = new ArrayList<>();
                    destinationsFrom.add(to);
                    destinations.put(from, destinationsFrom);
                }
                else
                {
                    destinationsFrom = destinations.get(from);
                    if(destinationsFrom == null)
                    {
                        destinationsFrom = new ArrayList<>();
                    }
                    destinationsFrom.add(to);
                    destinations.put(from, destinationsFrom);
                }
                int transfer_type = scanner.nextInt();
                if(scanner.hasNext())
                {
                    int time = scanner.nextInt();
                    int minutes = time/60;
                    int hour = minutes/60;
                    int second = time%60;
                    Time time1 = new Time(hour, minutes, second);
                    EdgeJourney currentEdge  = new EdgeJourney (from, to, time1, transfer_type);
                    List<EdgeJourney> list = adjacencyList.get(from);
                    if(list == null)
                    {
                        list = new ArrayList<EdgeJourney>();
                    }
                    list.add(currentEdge);
                    adjacencyList.put(from, list);
                    //minTransferTimeMatrix[from][to] = time1;
                    //Time time2 = new Time(0, 0, 0);
                    //minTransferTimeMatrix[from][to] = time2;
                }
                else
                {
                    Time time1 = new Time(0, 0, 0);
                    EdgeJourney currentEdge  = new EdgeJourney (from, to, time1, transfer_type);
                    List<EdgeJourney> list = adjacencyList.get(from);
                    if(list == null)
                    {
                        list = new ArrayList<EdgeJourney>();
                    }
                    list.add(currentEdge);
                    adjacencyList.put(from, list);
                }

                scanner.close();
                line = reader.readLine();
            }

        }
        catch(Exception e)
        {
            valid = false;
        }
    }
}
