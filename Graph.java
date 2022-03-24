import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    final double infinity = Double.POSITIVE_INFINITY;
    Time timeClass;
    boolean valid = true;
    //list of all the bus stops (used for part 2)
    ArrayList<BusStop> allBusStops = new ArrayList<BusStop>();
    ArrayList<Integer> trip_ids = new ArrayList<Integer>();
    //integer represents the node that the edge is coming from, the edge list is the egdes that can be reached from the first node
    public static HashMap<Integer, ArrayList<Integer>> destinations = new HashMap<>();
    public static HashMap<Integer, ArrayList<EdgeJourney>> adjacencyList = new HashMap<>();
    public static HashMap<Integer, Integer> transferType = new HashMap<>();
    public static Map<Time, ArrayList<BusStop>> arrivalTimeList;
    public static Map<Time, ArrayList<BusStop>> departureTimeList;
    //this map has a trip id and an array list of all the stops that the trip goes by
    public static HashMap<Integer, ArrayList<Integer>> stopsOnTrip = new HashMap<>();
    //this matrix represents the distance between two nodes i and j. The distance is stored in matrix[i][j]
    //Time[][] minTransferTimeMatrix = new Time[allBusStops.size()][allBusStops.size()];

    Graph(String transfersFile, String stopsFile, String stopsTime) throws IOException {//if the file name is null, we cannot read from the file and so the graph cannot be made
        //fill up the list of all the bus stops
        if (stopsFile == null || stopsFile == "") {
            valid = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(stopsFile));
            String line = reader.readLine();
            //skipping the first line because this is just the headings
            line = reader.readLine();

            while(line != null)
            {
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

        //reading in the stops time file
        if(stopsTime == null || stopsTime == "")
        {
            valid = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(stopsTime));
            String line = reader.readLine();
            line = reader.readLine();

            while(line != null)
            {
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int trip_id = scanner.nextInt();
                trip_ids.add(trip_id);

                //getting arrival and departure time
                String arrival_time = scanner.next();
                Time arrival = timeClass.GetTimeFromString(arrival_time);
                String departure_time = scanner.next();
                Time departure = timeClass.GetTimeFromString(departure_time);
                if(arrival == null || departure == null)
                {
                    continue;
                }

                //adding the stop to the trip id list
                int stop_id = scanner.nextInt();
                if(stopsOnTrip == null || stopsOnTrip.isEmpty())
                {
                    ArrayList<Integer> bussesOnRoute = new ArrayList<>();
                    bussesOnRoute.add(stop_id);
                    stopsOnTrip.put(trip_id, bussesOnRoute);
                }
                else
                {
                    ArrayList<Integer> bussesOnRoute = stopsOnTrip.get(trip_id);
                    if(bussesOnRoute == null)
                    {
                        bussesOnRoute = new ArrayList<>();
                    }
                    bussesOnRoute.add(stop_id);
                    stopsOnTrip.put(trip_id, bussesOnRoute);
                }

                //adding the time and the stop to the arrival time list
                BusStop currentStop = getBusStopByID(stop_id);
                if(arrivalTimeList == null || arrivalTimeList.isEmpty())
                {
                    ArrayList<BusStop> bussesArrivingAt = new ArrayList<>();
                    bussesArrivingAt.add(currentStop);
                    arrivalTimeList.put(arrival, bussesArrivingAt);
                }
                else
                {
                    ArrayList<BusStop> bussesArrivingAt = arrivalTimeList.get(arrival);
                    if(bussesArrivingAt == null)
                    {
                        bussesArrivingAt = new ArrayList<BusStop>();
                    }
                    bussesArrivingAt.add(currentStop);
                    arrivalTimeList.put(arrival, bussesArrivingAt);
                }

                //adding the time and the stop to the departure time list
                if (departureTimeList == null || departureTimeList.isEmpty())
                {
                    ArrayList<BusStop> bussesDepartingAt = new ArrayList<>();
                    bussesDepartingAt.add(currentStop);
                    departureTimeList.put(departure, bussesDepartingAt);
                }
                else
                {
                    ArrayList<BusStop> bussesDepartingAt = departureTimeList.get(arrival);
                    if(bussesDepartingAt == null)
                    {
                        bussesDepartingAt = new ArrayList<BusStop>();
                    }
                    bussesDepartingAt.add(currentStop);
                    departureTimeList.put(arrival, bussesDepartingAt);

                }
                scanner.close();
                line = reader.readLine();
            }

            //make adjacency list now
            for(int i = 0; i < trip_ids.size(); i++)
            {
                int currentTripID = trip_ids.get(i);
                ArrayList<Integer> stopsOnTripID = stopsOnTrip.get(currentTripID);
                for(int j = 0; j < stopsOnTripID.size(); j++)
                {
                    for(int k = 1; j + k < stopsOnTripID.size(); k++)
                    {
                        EdgeJourney journey = new EdgeJourney(stopsOnTripID.get(j), stopsOnTripID.get(j+k), 1, 1);
                        journey.addJourneyID(currentTripID);
                        if(adjacencyList.get(stopsOnTripID.get(j)) == null)
                        {
                            ArrayList<EdgeJourney> reachableStops = new ArrayList<EdgeJourney>();
                            reachableStops.add(journey);
                        }
                        else
                        {
                            ArrayList<EdgeJourney> reachableStops = adjacencyList.get(stopsOnTripID.get(j));
                            reachableStops.add(journey);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            valid = false;
        }

        //reading in transfers file
        if (transfersFile == null || transfersFile == "") {
            valid = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(stopsTime));

            String line = reader.readLine();
            line = reader.readLine();

            int count = 0;
            while (line != null && valid && count < 5084) {
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                count ++;

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
                    EdgeJourney currentEdge  = new EdgeJourney (from, to, transfer_type, 1);
                    currentEdge.addJourneyTime(time1);
                    ArrayList<EdgeJourney> list = adjacencyList.get(from);
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
                    EdgeJourney currentEdge  = new EdgeJourney (from, to, transfer_type, 1);
                    currentEdge.addJourneyTime(time1);
                    ArrayList<EdgeJourney> list = adjacencyList.get(from);
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

    //TODO : Dijkstra

    //TODO: shortest path between two nodes

    boolean directJourney(int start_id, int destination_id)
    {
        ArrayList<Integer> stops = destinations.get(start_id);
        boolean directJourney = stops.contains(destination_id);
        return directJourney;
    }

    public BusStop getBusStopByID(int id)
    {
        //Collections.sort(list, BusStop.BusStopIDComparator);
        //chaning over to an array cost the array list is causing problems not deleting what was already exisitng in the place im adding to
        BusStop[] busStopList = new BusStop[allBusStops.size()];
        for(int i = 0; i < allBusStops.size(); i++)
        {
            busStopList[i] = allBusStops.get(i);
        }
        busStopList = sortArrayByStopID(busStopList);
        return binarySearchByStopID(id, busStopList);
    }

    public BusStop[] sortArrayByStopID(BusStop[] busStops)
    {
        BusStop[] copy = busStops.clone();
        mergeSort(busStops, copy, 0, busStops.length-1);
        return busStops;
    }

    private static void mergeSort(BusStop[] a, BusStop[] copy, int lo, int hi)
    {
        if(lo < hi)
        {
            int mid = lo + (hi - lo) / 2;
            //recursively sort LHS
            mergeSort(a, copy, lo, mid);
            //recursively sort RHS
            mergeSort(a, copy, mid + 1, hi);
            //merge the sorted halves
            merge(a, copy, lo, mid, hi);
        }
    }

    private static void merge(BusStop[] a, BusStop[] copy, int lo, int mid, int high)
    {
        if(high + 1 - lo >= 0)
        {
            System.arraycopy(a, lo, copy, lo, high + 1 -lo);
        }

        int i = lo;
        int j = mid + 1;
        int k;

        for (k = lo; k <= high; k++)
        {
            if (i > mid)
            {
                a[k] = copy[j++];
            }
            else if (j > high)
            {
                a[k] = copy[i++];
            }
            //compare based on stop ID.
            else if (a[j].stop_id < copy[i].stop_id)
            {
                a[k] = copy[j++];
            }
            else a[k] = copy[i++];
        }
    }

    public BusStop binarySearchByStopID(int stop_id, BusStop[] busStops)
    {
        int low = 0;
        int high = busStops.length;
        while(low <= high)
        {
            int mid = low + (high-low) / 2;
            if(stop_id < busStops[mid].stop_id)
            {
                high = mid -1;
            }
            else if (stop_id > busStops[mid].stop_id)
            {
                low = mid + 1;
            }
            else if(stop_id == busStops[mid].stop_id)
            {
                return busStops[mid];
            }
        }
        return null;
    }

    public ArrayList<BusStop> getStopsForBusArrivingAt(Time time)
    {

        return arrivalTimeList.get(time);
    }

    public void tellMeAboutBussesArrivingAt(Time time)
    {
        ArrayList<BusStop> buses = getStopsForBusArrivingAt(time);
        for(int i = 0; i < buses.size(); i++)
        {
            BusStop currentBusStop = buses.get(i);
            currentBusStop.tellMeAboutThisBusStop();
        }

    }



}
