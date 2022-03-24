import java.util.ArrayList;

public class Trip {
    int trip_id;
    ArrayList<Time> arrival_times = new ArrayList<Time>();
    ArrayList<Time> departure_times = new ArrayList<Time>();
    ArrayList<Integer> stop_sequence = new ArrayList<>();

    Trip(int trip_id)
    {
        this.trip_id = trip_id;
    }

    void addToTrip(Time arrival_time, Time departure_time, int stop_id)
    {
        arrival_times.add(arrival_time);
        departure_times.add(departure_time);
        stop_sequence.add(stop_id);

    }

    int getStopForArrivalTime(Time time)
    {
        boolean contains = arrival_times.contains(time);
        if(contains)
        {
            int index = arrival_times.indexOf(time);
            return stop_sequence.get(index);
        }
        else
        {
            return -1;
        }
    }




}
