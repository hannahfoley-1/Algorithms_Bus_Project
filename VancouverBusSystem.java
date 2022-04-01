import java.io.IOException;
import java.net.SecureCacheResponse;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class VancouverBusSystem {
    // public static BusStopsList allBusStops;
    //public static Graph transfersGraph;
    public static EdgeWeightedGraph graph;
    //public static BusStopsSchedule schedule;


    public static void main(String[] args) {
        {
            System.out.println("Begin");
            try {
                //transfersGraph = new Graph("transfers.txt", "stops.txt", "stop_times.txt");
                graph = new EdgeWeightedGraph("stops.txt", "stop_times_small.txt", "transfers.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean quit = false;
        Scanner scanner = new Scanner(System.in);
        String input;

        while(!quit)
        {
            System.out.print("What functionality would you like to use? \n 1. Find the shortest route between two bus " +
                    "stops and the cost. \n 2. Return information about a given stop. \n 3. Search for all trips with a " +
                    "given arrival time. \n Please enter the number of the functionality you would like to use or type 'quit' " +
                    "to leave the program " );
            input = scanner.next();

            if(input.equalsIgnoreCase("quit"))
            {
                quit = true;
                System.out.print("Bye, thank you :D ");
            }
            else if (input.equals("1") || input.equalsIgnoreCase("one"))
            {
                System.out.println("You have chosen functionality 1. ");
                shortest_path_option();
            }
            else if (input.equals("2") || input.equalsIgnoreCase("two"))
            {
                System.out.println("You have chosen functionality 2. ");
                information_option();
            }
            else if (input.equals("3") || input.equalsIgnoreCase("three"))
            {
                System.out.println("You have chosen functionality 3. ");
                arrival_time_option();
            }

        }
    }

    public static void shortest_path_option()
    {
        Scanner scanner = new Scanner(System.in);
        int start_id = 0;
        int destination_id = 0;

        System.out.print("What bus stop are you travelling from? ");
        if(scanner.hasNextInt())
        {
            start_id = scanner.nextInt();
        }
        else
        {
            System.out.println("Please enter bus stop ID number ");
        }

        //BusStop start = allBusStops.getBusStopByID(start_id);

        System.out.print("What bus stop are you travelling to? ");
        if(scanner.hasNextInt())
        {
            destination_id = scanner.nextInt();
        }
        else
        {
            System.out.println("Please enter bus stop ID number ");
        }

        graph.ShortestPath(start_id, destination_id);

        /*
        //check if a direct route is possible using directTrip() function in edge weidghted grpah class
        boolean direct = graph.directTrip(start_id, destination_id);

        if(direct)
        {
            System.out.println("A direct journey exists between these two bus stops.");
            System.out.println("This is on route " + graph.directTripID(start_id, destination_id));
            System.out.println("There are " + graph.numberOfStopsBetweenDirect(start_id, destination_id) + " stops in between " +
                    "the two stops");
            ArrayList<Integer> stops = graph.directTripStopsBetweenArray(start_id, destination_id);
            System.out.println("These stops are: ");
            for(int i = 0; i < stops.size(); i++)
            {
                System.out.println(stops.get(i));
            }
            System.out.println("The price of this journey is " ); //TODO
        }
        else
        {
            System.out.println("No direct journey exists between these two stops.");
            ShortestPath SP = new ShortestPath(graph, start_id);
            System.out.println("The distance travelled will be " + SP.getShortestDistBetween(start_id, destination_id));
            ArrayList<Integer> stops = SP.getStopSequenceForTrip(start_id, destination_id);
            System.out.println("The stops are: ");
            for(int i = 0; i < stops.size(); i++)
            {
                System.out.println(stops.get(i));
            }
            System.out.println("The price of this journey is " ); //TODO
        }

        //if it is possible, calculate and return price

        //else

        //BusStop destination = allBusStops.getBusStopByID(destination_id);

        //TODO: Shortest path between these two stops
*/
    }

    public static void information_option()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the bus stop or the first few letters ");
        String input;
        input = scanner.nextLine();
        input = input.toUpperCase(Locale.ROOT);
        TST tst = new TST(graph);
        ArrayList<String> matches = tst.contains(input);
        tst.printPossibleValues();

        /*
        if(stopsWithPrefix != null)
        {
            for(String i : stopsWithPrefix)
            {
                System.out.println(tst.get(i).tellMeAboutThisBusStop());
            }
        }
        else
        {
            System.out.println("No matches for this stop ");
        }

         */

    }

    public static void arrival_time_option()
    {
        Scanner scanny = new Scanner(System.in);
        String input;
        System.out.print("Enter arrival time in hh:mm:ss ");
        input = scanny.next();
        Scanner scanner = new Scanner(input);
        scanner.useDelimiter(":");
        int hours = scanner.nextInt();
        int minutes = scanner.nextInt();
        int seconds = 0;
        if(scanner.hasNextInt())
        {
            seconds = scanner.nextInt();
        }
        Time time = new Time(hours, minutes, seconds);
        graph.getBussesArrivingAt(time);
        /*
        ArrayList<Trip> allTrips = graph.allTrips;
        for(int i = 0; i < allTrips.size(); i++)
        {
            Trip currentTrip = allTrips.get(i);
            int busStop = currentTrip.getStopForArrivalTime(time);
            if(busStop != -1)
            {
                System.out.println("Stop number: " + busStop + ", Trip ID: " + currentTrip.trip_id);
            }
        }
         */
        //schedule.tellMeAboutBussesArrivingAt(time);
    }


}

