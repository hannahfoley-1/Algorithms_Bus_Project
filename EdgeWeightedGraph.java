import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class EdgeWeightedGraph {
    public int noVertices;
    Time timeClass = new Time(0, 0, 0);
    public ArrayList<BusStop> allBusStops;
    //array list of all the trips, trips are equivalent to bus routes
    public ArrayList<Trip> allTrips;
    //array list of all journeys, journeys are any distance travelled between 2 stops
    public ArrayList<EdgeJourney> allJourneys;
    //this adjacency list represents the stop number, and all the accessible journeys from that stop number
    HashMap<Integer, ArrayList<EdgeJourney>> adjacencyList;

    public EdgeWeightedGraph(String stopsfile, String stopsTimesFile, String transfersFile)
    {
        allBusStops = new ArrayList<>();
        fillUpStops(stopsfile);
        this.noVertices = allBusStops.size();
        allTrips = new ArrayList<>();
        allJourneys = new ArrayList<>();
        adjacencyList = new HashMap<>();
        fillUpTrips(stopsTimesFile);
        //adjacencyList = new ArrayList<ArrayList<EdgeJourney>>();

    }

    public void fillUpStops(String stopsFile)
    {
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
            while(line != null)
            {
                System.out.println(count);
                count++;
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int stop_id = scanner.nextInt();
                //skipping irrelevant info
                scanner.next();
                String stop_name = scanner.next();
                //skipping irrelevant info (i hope)
                scanner.next();
                double stop_lat = scanner.nextDouble();
                double stop_lon = scanner.nextDouble();
                BusStop currentStop = new BusStop(stop_id, stop_lat, stop_lon, stop_name);
                allBusStops.add(currentStop);
                scanner.close();
                line = reader.readLine();
            }

        }
        catch (Exception e)
        {
            valid = false;
        }
    }

    public void fillUpTrips(String filename)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            line = reader.readLine();

            Scanner scanner = new Scanner(line);
            scanner.useDelimiter(",");

            //the current trip we are looking at, this will change
            int working_trip_id = scanner.nextInt();
            String arrival_time = scanner.next();
            Time arrival = timeClass.GetTimeFromString(arrival_time);
            String departure_time = scanner.next();
            Time departure = timeClass.GetTimeFromString(departure_time);
            int stop_id = scanner.nextInt();
            Trip currentTrip = new Trip(working_trip_id);
            currentTrip.addToTrip(arrival, departure, stop_id);

            line = reader.readLine();
            double prev_dist_travelled = 0;

            while(line != null)
            {
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int trip_id_2 = scanner.nextInt();
                if(working_trip_id == trip_id_2)
                {
                    //they are still on the same route
                    arrival_time = scanner.next();
                    arrival = timeClass.GetTimeFromString(arrival_time);
                    departure_time = scanner.next();
                    departure = timeClass.GetTimeFromString(departure_time);
                    int stop_id2 = scanner.nextInt();
                    currentTrip.addToTrip(arrival, departure, stop_id2);

                    //this adds to the adjacency list
                    //if they're on the same route, there will be a distance travelled part
                    //skip over stop_sequence
                    scanner.next();
                    //skip over stop_headsign
                    scanner.next();
                    //skip over pick up type and drop of type
                    scanner.next();
                    scanner.next();
                    double dist_travelled = scanner.nextDouble();
                    dist_travelled -= prev_dist_travelled;
                    //making a new edge
                    EdgeJourney edge = new EdgeJourney(stop_id, stop_id2, 1, dist_travelled);
                    allJourneys.add(edge);
                    //adding to the adjacency list
                    if(adjacencyList == null)
                    {
                        ArrayList<EdgeJourney> journeys = new ArrayList<>();
                        journeys.add(edge);
;                       adjacencyList.put(stop_id, journeys);
                    }
                    else
                    {
                        ArrayList<EdgeJourney> journeys;
                        if(adjacencyList.get(stop_id) == null)
                        {
                            journeys = new ArrayList<>();
                        }
                        else
                        {
                            journeys = adjacencyList.get(stop_id);
                        }
                        journeys.add(edge);
                        adjacencyList.put(stop_id, journeys);
                    }

                    line = reader.readLine();
                }
                else
                {
                    //we are working with a new trip so add the previous trip to the list of finished trips
                    allTrips.add(currentTrip);
                    //change working trip
                    working_trip_id = trip_id_2;
                    currentTrip = new Trip(working_trip_id);
                    arrival_time = scanner.next();
                    arrival = timeClass.GetTimeFromString(arrival_time);
                    departure_time = scanner.next();
                    departure = timeClass.GetTimeFromString(departure_time);
                    stop_id = scanner.nextInt();
                    currentTrip.addToTrip(arrival, departure, stop_id);
                    line = reader.readLine();
                }
                scanner.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
    }

    public boolean directTrip(int start_id, int end_id)
    {
        for(int i = 0; i < allTrips.size(); i++)
        {
            Trip currentTrip = allTrips.get(i);
            ArrayList<Integer> stopsOnThisRoute = currentTrip.stop_sequence;
            if(stopsOnThisRoute.contains(start_id) && stopsOnThisRoute.contains(end_id))
            {
                return true;
            }
        }
        return false;
    }

    public int directTripID(int start_id, int end_id)
    {
        for(int i = 0; i < allTrips.size(); i++)
        {
            Trip currentTrip = allTrips.get(i);
            ArrayList<Integer> stopsOnThisRoute = currentTrip.stop_sequence;
            if(stopsOnThisRoute.contains(start_id) && stopsOnThisRoute.contains(end_id))
            {
                return currentTrip.trip_id;
            }
        }
        return 0;
    }

    public ArrayList<Integer> directTripStopsBetweenArray(int start_id, int end_id)
    {
        for(int i = 0; i < allTrips.size(); i++)
        {
            Trip currentTrip = allTrips.get(i);
            ArrayList<Integer> stopsOnThisRoute = currentTrip.stop_sequence;
            if(stopsOnThisRoute.contains(start_id) && stopsOnThisRoute.contains(end_id))
            {
                return stopsOnThisRoute;
            }
        }
        return null;
    }

    public int numberOfStopsBetweenDirect(int start_id, int end_id)
    {
        for(int i = 0; i < allTrips.size(); i++)
        {
            Trip currentTrip = allTrips.get(i);
            ArrayList<Integer> stopsOnThisRoute = currentTrip.stop_sequence;
            if(stopsOnThisRoute.contains(start_id) && stopsOnThisRoute.contains(end_id))
            {
                int index1 = stopsOnThisRoute.indexOf(start_id);
                int index2 = stopsOnThisRoute.indexOf(end_id);
                return end_id - start_id;
            }
        }
        return -1;
    }



    public int findStopIndex(int stop_id)
    {
        for (int i = 0; i < allBusStops.size(); i++)
        {
            if(allBusStops.get(i).stop_id == stop_id)
            {
                return i;
            }
        }
        return -1;
    }


}
