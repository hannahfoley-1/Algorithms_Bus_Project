import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    final double infinity = Double.POSITIVE_INFINITY;
    boolean valid = true;
    //integer represents the node that the edge is coming from, the edge list is the egdes that can be reached from the first node
    public static Map<Integer, List<EdgeJourney>> adjacencyList;
    //this matrix represents the distance between two nodes i and j. The distance is stored in matrix[i][j]
    Time[][] minTransferTimeMatrix;

    Graph(String filename) throws IOException {//if the file name is null, we cannot read from the file and so the graph cannot be made
        if (filename == null || filename == "") {
            valid = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line = reader.readLine();
            line = reader. readLine();
            //the matrix is made up of an integer array which contains two integers : the source and the destination intersections
            // matrix [source][destination] will give the distance between the source and the destination node

            //the matrix isn't completely space efficient but searching using the matrix is very efficient

            //initialise all of matrix elements as positive infinity
            //go through matrix and make empty spaces infinity

            while (line != null && valid) {
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                int transfer_type = scanner.nextInt();
                if(scanner.hasNext())
                {
                    Scanner scanner2 = new Scanner(line);
                    scanner2.useDelimiter(":");
                    int hour = scanner.nextInt();
                    int minute = scanner.nextInt();
                    int second = scanner.nextInt();
                    Time time = new Time(hour, minute, second);
                    EdgeJourney currentEdge  = new EdgeJourney (from, to, time);
                    List<EdgeJourney> list = adjacencyList.get(from);
                    if(list == null)
                    {
                        list = new ArrayList<EdgeJourney>();
                    }
                    list.add(currentEdge);
                    adjacencyList.put(from, list);
                    minTransferTimeMatrix[from][to] = time;
                    Time time1 = new Time(0, 0, 0);
                    minTransferTimeMatrix[from][to] = time1;
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
