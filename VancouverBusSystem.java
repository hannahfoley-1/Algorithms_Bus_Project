import java.io.IOException;
import java.util.Scanner;

public class VancouverBusSystem {
    public static BusStopsList allBusStops;
    public static Graph transfersGraph;
    public static BusStopsSchedule schedule;


    public static void main(String[] args) {
        {
            try {
                allBusStops = new BusStopsList("stops.txt");
                transfersGraph = new Graph("transfers.txt");
                schedule = new BusStopsSchedule("stop_times.txt", allBusStops);
            } catch (IOException e) {
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
        String input;

        System.out.print("What bus stop are you travelling from? ");
        //error handle and get from

        System.out.print("What bus stop are you travelling to? ");
        //error handle and get this

        //make two bus stop objects from it

    }

    public static void information_option()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
    }

    public static void arrival_time_option()
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.print("Enter arrival time in hh:mm:ss ");
        scanner.useDelimiter(":");
        int hours = scanner.nextInt();
        int minutes = scanner.nextInt();
        int seconds = scanner.nextInt();
        Time time = new Time(hours, minutes, seconds);
        schedule.tellMeAboutBussesArrivingAt(time);
    }


}
