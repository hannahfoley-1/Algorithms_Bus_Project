import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BusStopsSchedule {
    public static Map<Time, ArrayList<BusStop>> arrivalTimeList;
    public static Map<Time, ArrayList<BusStop>> departureTimeList;
    public static BusStopsList busStopsList;
    boolean valid = true;

    BusStopsSchedule(String filename, BusStopsList busStopsList){
        this.busStopsList = busStopsList;
        if (filename == null || filename == "") {
            valid = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();

            while(line != null)
            {
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                scanner.next();
                String arrival_time = scanner.next();
                Scanner scanner2 = new Scanner(arrival_time);
                scanner2.useDelimiter(":");
                int hours = scanner2.nextInt();
                int minutes = scanner2.nextInt();
                int seconds = scanner2.nextInt();
                Time arrival = new Time(hours, minutes, seconds);
                String departure_time = scanner.next();
                scanner2 = new Scanner(departure_time);
                scanner2.useDelimiter(":");
                hours = scanner2.nextInt();
                minutes = scanner2.nextInt();
                seconds = scanner2.nextInt();
                Time departure = new Time(hours, minutes, seconds);
                int stop_id = scanner.nextInt();
                BusStop currentStop = busStopsList.getBusStopByID(stop_id);
                ArrayList<BusStop> bussesArrivingAt = arrivalTimeList.get(arrival);
                bussesArrivingAt.add(currentStop);
                arrivalTimeList.put(arrival, bussesArrivingAt);
                ArrayList<BusStop> bussesDepartingAt = departureTimeList.get(departure);
                bussesDepartingAt.add(currentStop);
                departureTimeList.put(departure, bussesDepartingAt);
                scanner.close();
                scanner2.close();
                line = reader.readLine();

            }

        }
        catch (Exception e)
        {
            valid = false;
        }

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
