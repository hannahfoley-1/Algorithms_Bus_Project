import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class BusStopsSchedule {
    public static Map<Time, ArrayList<BusStop>> arrivalTimeList;
    //public static Map<Time, ArrayList<BusStop>> departureTimeList;
    public static BusStopsList busStopsList;
    boolean valid = true;

    BusStopsSchedule(String filename, BusStopsList busStopsList){
        this.busStopsList = busStopsList;
        if (filename == null || filename == "") {
            valid = false;
        }

        arrivalTimeList = new HashMap<>();
        //departureTimeList = new HashMap<>();
        this.busStopsList = busStopsList;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            line = reader.readLine();

            int count = 0;
            while(line != null)
            {
                System.out.println(count++);
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(",");
                scanner.next();
                String arrival_time = scanner.next();
                arrival_time = arrival_time.substring(1);
                //this line makes sure that we skip the space after the comman, otherwise the next line wont work
                String[] time = arrival_time.split(":");
                int hours = Integer.parseInt(time[0]);
                int minutes = Integer.parseInt(time[1]);
                int seconds = Integer.parseInt(time[2]);
                if(hours > 23 || minutes > 59 || seconds > 59 || hours < 0 || minutes < 0 || seconds < 0)
                {
                    continue;
                }
                Time arrival = new Time(hours, minutes, seconds);
                String departure_time = scanner.next();
                departure_time = departure_time.substring(1);
                String [] time2 = departure_time.split(":");
                hours = Integer.parseInt(time2[0]);
                minutes = Integer.parseInt(time2[1]);
                seconds = Integer.parseInt(time2[2]);
                if(hours > 23 || minutes > 59 || seconds > 59 || hours < 0 || minutes < 0 || seconds < 0)
                {
                    continue;
                }
                Time departure = new Time(hours, minutes, seconds);
                int stop_id = scanner.nextInt();
                BusStop currentStop = busStopsList.getBusStopByID(stop_id);
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
                /*
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

                 */
                scanner.close();
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
