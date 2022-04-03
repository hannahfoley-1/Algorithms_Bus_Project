import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLOutput;
import java.util.*;

public class EdgeWeightedGraph {
    public int noVertices;
    //public ArrayList<BusStop> allBusStops;
    HashMap<Integer, BusStop> allBusStops;
    //public ArrayList<Trip> allTrips;
    HashMap<Integer, Trip> allTrips;
    HashMap<Integer, ArrayList<EdgeJourney>> adjacencyList;
    Time timeClass = new Time(0, 0, 0);
    int minStopNo = Integer.MAX_VALUE;
    int maxStopNo = Integer.MIN_VALUE;
    int minTripNo = Integer.MAX_VALUE;
    int maxTripNo = Integer.MIN_VALUE;

    EdgeWeightedGraph(String stopsfile, String stopsTimesFiles, String transfersFile) {
        allBusStops = new HashMap<>();
        fillUpStops(stopsfile);
        this.noVertices = allBusStops.size();
        allTrips = new HashMap<>();
        adjacencyList = new HashMap<>();
        fillUpTrips(stopsTimesFiles, transfersFile);

    }

    private void fillUpStops(String stopsFile) {
        boolean valid;
        //fill up the list of all the bus stops
        if (stopsFile == null || stopsFile == "") {
            valid = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(stopsFile));
            String line = reader.readLine();
            //skipping the first line because this is just the headings
            line = reader.readLine();

            int count = 0;
            while (line != null) {
                System.out.println(count);
                count++;
                System.out.println(count);
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int stop_id = scanner.nextInt();
                if(stop_id < minStopNo)
                {
                    minStopNo = stop_id;
                }
                if(stop_id > maxStopNo)
                {
                    maxStopNo = stop_id;
                }
                //skipping irrelevant info
                scanner.next();
                String stop_name = scanner.next();
                //skipping irrelevant info (i hope)
                scanner.next();
                double stop_lat = scanner.nextDouble();
                double stop_lon = scanner.nextDouble();
                BusStop currentStop = new BusStop(stop_id, stop_lat, stop_lon, stop_name);
                allBusStops.put(stop_id, currentStop);
                scanner.close();
                line = reader.readLine();
            }

        } catch (Exception e) {
            valid = false;
        }
    }

    private void fillUpTrips(String stopsTimesFile, String transfersFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(stopsTimesFile));
            String line = reader.readLine();
            line = reader.readLine();

            Scanner scanner = new Scanner(line);
            scanner.useDelimiter(",");

            //the current trip we are looking at, this will change
            int working_trip_id = scanner.nextInt();
            minTripNo = working_trip_id;
            maxTripNo = working_trip_id;
            String arrival_time = scanner.next();
            Time arrival = timeClass.GetTimeFromString(arrival_time);
            String departure_time = scanner.next();
            Time departure = timeClass.GetTimeFromString(departure_time);
            int stop_id1 = scanner.nextInt();
            Trip currentTrip = new Trip(working_trip_id);
            currentTrip.addToTrip(arrival, departure, stop_id1);

            line = reader.readLine();
            double prev_dist_travelled = 0;
            int stop_id2 = 0;

            int count = 0;
            while (line != null) {
                System.out.println("Stopsfile: " + count);
                count++;
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int trip_id_2 = scanner.nextInt();
                if(trip_id_2 < minTripNo)
                {
                    minTripNo = trip_id_2;
                }
                if(trip_id_2 > maxTripNo)
                {
                    maxTripNo = trip_id_2;
                }
                if (working_trip_id == trip_id_2)
                {
                    //they are still on the same route
                    arrival_time = scanner.next();
                    arrival = timeClass.GetTimeFromString(arrival_time);
                    departure_time = scanner.next();
                    departure = timeClass.GetTimeFromString(departure_time);
                    stop_id2 = scanner.nextInt();
                    currentTrip.addToTrip(arrival, departure, stop_id2);

                } else {
                    //we are working with a new trip so add the previous trip to the list of finished trips
                    allTrips.put(working_trip_id, currentTrip);
                    //change working trip
                    working_trip_id = trip_id_2;
                    currentTrip = new Trip(working_trip_id);
                    arrival_time = scanner.next();
                    arrival = timeClass.GetTimeFromString(arrival_time);
                    departure_time = scanner.next();
                    departure = timeClass.GetTimeFromString(departure_time);
                    stop_id2 = scanner.nextInt();
                    currentTrip.addToTrip(arrival, departure, stop_id2);
                }
                //this adds to the adjacency list
                //if they're on the same route, there will be a distance travelled part
                //skip over stop_sequence
                scanner.next();
                //skip over stop_headsign
                scanner.next();
                //skip over pick up type and drop of type
                scanner.next();
                scanner.next();
                //skip over distance travelled for the min
                double dist_travelled = 0;
                if(scanner.hasNext())
                {
                    dist_travelled = scanner.nextDouble();
                    prev_dist_travelled = dist_travelled;
                    dist_travelled -= prev_dist_travelled;
                }

                //making a new edge
                EdgeJourney edge = new EdgeJourney(stop_id1, stop_id2, 1, 1);
                edge.addDistance(dist_travelled);
                //adding to the adjacency list
                if (adjacencyList == null) {
                    ArrayList<EdgeJourney> journeys = new ArrayList<>();
                    journeys.add(edge);
                    adjacencyList.put(stop_id1, journeys);
                } else {
                    ArrayList<EdgeJourney> journeys;
                    if (adjacencyList.get(stop_id1) == null) {
                        journeys = new ArrayList<>();
                    } else {
                        journeys = adjacencyList.get(stop_id1);
                    }
                    //check if the edge already exists at this point in the adjacency list
                    boolean exists = false;
                    for(int i = 0; i < journeys.size(); i++)
                    {
                        EdgeJourney thisEdge = journeys.get(i);
                        if(thisEdge.to_stop_id == edge.to_stop_id && thisEdge.from_stop_id == edge.from_stop_id)
                        {
                            exists = true;
                        }
                    }
                    if(!exists)
                    {
                        journeys.add(edge);
                        adjacencyList.put(stop_id1, journeys);
                    }
                }
                stop_id1 = stop_id2;
                line = reader.readLine();
                scanner.close();
            }
            //add the final trip we were working on
            allTrips.put(working_trip_id, currentTrip);
        } catch (Exception e) {
            System.out.println("Error");
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(transfersFile));
            String line = reader.readLine();
            line = reader.readLine();

            int count = 0;
            while (line != null) {
                System.out.println("Transfersfile: " + count);
                count++;
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");

                int arrival_stop_id = scanner.nextInt();
                int departure_stop_id = scanner.nextInt();
                int transfer_type = scanner.nextInt();
                double transferWeight = 0;

                if (transfer_type == 0) {
                    transferWeight = 2;
                } else if (transfer_type == 2) {
                    int minimumTransferTime = scanner.nextInt();
                    transferWeight = minimumTransferTime / 100;
                }
                EdgeJourney edge = new EdgeJourney(arrival_stop_id, departure_stop_id, transfer_type, transferWeight);
                //add this edge to the adjacency list
                if (adjacencyList == null) {
                    ArrayList<EdgeJourney> journeys = new ArrayList<>();
                    journeys.add(edge);
                    adjacencyList.put(arrival_stop_id, journeys);
                } else {
                    ArrayList<EdgeJourney> journeys;
                    if (adjacencyList.get(arrival_stop_id) == null) {
                        journeys = new ArrayList<>();
                    } else {
                        journeys = adjacencyList.get(arrival_stop_id);
                    }
                    //check if the edge already exists at this point in the adjacency list
                    boolean exists = false;
                    for(int i = 0; i < journeys.size(); i++)
                    {
                        EdgeJourney thisEdge = journeys.get(i);
                        if(thisEdge.to_stop_id == edge.to_stop_id && thisEdge.from_stop_id == edge.from_stop_id)
                        {
                            exists = true;
                        }
                    }
                    if(!exists)
                    {
                        journeys.add(edge);
                        adjacencyList.put(arrival_stop_id, journeys);
                    }
                }
                line = reader.readLine();
                scanner.close();
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public void ShortestPath(int start, int end) {
        HashMap<Integer, Boolean> visited = new HashMap<>();
        HashMap<Integer, Integer> proceedingStop = new HashMap<>();
        HashMap<Integer, Double> distance = new HashMap<>();

        for (int i = minStopNo; i <= maxStopNo; i++) {
            if(allBusStops.get(i) != null)
            {
                int currentStopID = allBusStops.get(i).stop_id;
                visited.put(currentStopID, false);
                proceedingStop.put(currentStopID, -1);
                distance.put(currentStopID, Double.POSITIVE_INFINITY);
            }
        }
        distance.put(start, 0.0);

        PriorityQueue<EdgeJourney> queue = new PriorityQueue<EdgeJourney>(noVertices, new Comparator<EdgeJourney>() {
            @Override
            public int compare(EdgeJourney o1, EdgeJourney o2) {
                if (o1.edgeWeight < o2.edgeWeight) {
                    return -1;
                } else if (o1.edgeWeight > o2.edgeWeight) {
                    return 1;
                }
                return 0;
            }
        });

        int from = start;
        ArrayList<EdgeJourney> edges = adjacencyList.get(from);
        for (int i = 0; edges != null && i < edges.size(); i++) {
            queue.add(edges.get(i));
        }

        boolean solved = false;
        proceedingStop.put(start, start);

        while (!queue.isEmpty() && !solved) {
            //take out the edge that has the smallest distance
            EdgeJourney currentEdge = queue.poll();
            from = currentEdge.from_stop_id;
            int to = currentEdge.to_stop_id;

            //if we haven't visited already, mark as visited, check if the distance taken to get here is less than before
            if (!visited.get(to)) {
                visited.put(to, true);
                if (distance.get(from) + currentEdge.edgeWeight < distance.get(to)) {
                    distance.put(to, distance.get(from) + currentEdge.edgeWeight);
                }

                //add all the new edges from the node we just visited to the priority queue
                from = to;
                edges = adjacencyList.get(from);
                for (int i = 0; edges != null && i < edges.size() && from != start; i++) {
                    //if the edge goes to the starting node, don't take this edge
                    //if we've already visited this node, do not take this edge
                    if (edges.get(i).to_stop_id != start && !visited.get(edges.get(i).to_stop_id)) {
                        //get length from start node to the node
                        double soFar = distance.get(from);
                        soFar += edges.get(i).edgeWeight;
                        int transfer_type = edges.get(i).transfer_type;
                        EdgeJourney edgeForPQ = new EdgeJourney(start, edges.get(i).to_stop_id, transfer_type, soFar);
                        proceedingStop.put(edges.get(i).to_stop_id, edges.get(i).from_stop_id);
                        queue.add(edgeForPQ);
                    }
                }
            }
            //if (to == end) {
            if (visited.get(end)) {
                solved = true;
                System.out.println("The stops between stop #" + start + " (" + allBusStops.get(start).stop_name + ") " +
                        "and stop #" + end + " (" + allBusStops.get(end).stop_name + ") are: ");
                //reverse proceeding stop list
                if(proceedingStop.size() == 1)
                {
                    System.out.println("These are 1 stop apart");
                    System.out.println("//////////////////////");
                }
                else
                {
                    //print stops in correct order
                    ArrayList<Integer> stopSequence = new ArrayList<>();
                    int first = start;
                    int last = end;
                    while (allBusStops.get(last) != null && last != first) {
                        stopSequence.add(allBusStops.get(last).stop_id);
                        last = proceedingStop.get(last);
                    }
                    stopSequence.add(allBusStops.get(first).stop_id);
                    for(int i = stopSequence.size()-1; i >= 0; i--)
                    {
                        if(i != stopSequence.size()-1)
                        {
                            System.out.println("->");
                        }
                        System.out.println(stopSequence.get(i) + " (" + allBusStops.get(stopSequence.get(i)).stop_name + ")");

                    }
                    /*
                    int first = 0;
                            //= start;
                    //int last = end;
                    int last = start;
                    while (allBusStops.get(last) != null && last != first) {
                        //first != last &&
                        //&& last != end
                        if(last != end)
                        {
                            System.out.println("->");
                        }
                        System.out.println(last + " (" + allBusStops.get(last).stop_name + ")");
                        last = proceedingStop.get(last);
                        if(last == first)
                        {
                            System.out.println("->");
                            System.out.println(last + " (" + allBusStops.get(last).stop_name + ")");
                        }
                    }
                     */
                    System.out.println("The cost of this trip is " + distance.get(end));
                    System.out.println("//////////////////////////////////////////////");

                }
            }

        }
        if(!solved)
        {
            System.out.println("There doesn't seem to be a way to connect these two stops. Please try again.");
        }

        /*
    private double calcHueristic(int start, int end)
    {
        BusStop busStop1 = allBusStops.get(start);
        double lat1 = busStop1.stop_lat;
        double lon1 = busStop1.stop_lon;
        BusStop busStop2 = allBusStops.get(end);
        double lat2 = busStop2.stop_lat;
        double lon2 = busStop2.stop_lon;
        final int radius = 6271;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radius * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
*/

    }
    void getBussesArrivingAt(Time time)
    {
        boolean somethingPrinted = false;
        for(int i = minTripNo; i <= maxTripNo && allTrips.get(i) != null; i++)
        {
            Trip currentTrip = allTrips.get(i);
            //currentTrip.sortArrivalTimeArray();
            if(currentTrip.getStopForArrivalTime(time) != -1)
            //sort the arrival time array
            //currentTrip.arrival_times.sort(Comparator<Time>);
            //check if the time is in this array
            //if(currentTrip.arrival_times.contains(time))
            {
                somethingPrinted = true;
                System.out.println("Stop No: " + currentTrip.getStopForArrivalTime(time) + " Trip ID: " + currentTrip.trip_id);
            }
        }
        if(!somethingPrinted)
        {
            System.out.println("There are no buses arriving at this time. Maybe try and enter another time. ");
        }
    }

    boolean doesThisBusStopExist(int stop_id)
    {
        if(allBusStops.get(stop_id) != null)
        {
            return true;
        }
        return false;
    }
}
