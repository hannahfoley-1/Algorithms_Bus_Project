import java.nio.charset.MalformedInputException;
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

    //gets the stop that is arriving at the given time, if there is no trip that is arriving at this time, it returns -1
    //does this by searching through arrayList using binary search
    int getStopForArrivalTime(Time time)
    {
        if(time != null)
        {
            int low = 0;
            int high = arrival_times.size()-1;
            while(low < high)
            {
                int mid = low + (high-low) / 2;
                if(mid >= arrival_times.size() || arrival_times.get(mid) == null)
                {
                    return -1;
                }
                if(time.hour < arrival_times.get(mid).hour)
                {
                    high = mid - 1;
                }
                else if (time.hour > arrival_times.get(mid).hour)
                {
                    low = mid + 1;
                }
                else
                {
                    //now compare minutes
                    if(time.minute < arrival_times.get(mid).minute)
                    {
                        high = mid - 1;
                    }
                    else if (time.minute > arrival_times.get(mid).minute)
                    {
                        low = mid + 1;
                    }
                    else
                    {
                        //now compare seconds
                        if(time.second < arrival_times.get(mid).second)
                        {
                            high = mid - 1;
                        }
                        else if (time.second > arrival_times.get(mid).second)
                        {
                            low = mid + 1;
                        }
                        else
                        {
                            return stop_sequence.get(mid);
                        }
                    }
                }

            }
            if(low == high)
            {
                //if they are the same, return the stop
                Time couldBe = arrival_times.get(low);
                if(couldBe.minute == time.minute && couldBe.hour == time.hour && couldBe.second == time.second)
                {
                    return stop_sequence.get(low);
                }
            }
        }
        return -1;
    }

    void sortArrivalTimeArray()
    {
        //using selection sort
        Time mintime;
        mintime = arrival_times.get(1);
        int startOfUnsorted = 0;

        for(int i = startOfUnsorted; i < arrival_times.size() - 1 ;) {
            for (int j = startOfUnsorted + 1; j < arrival_times.size(); j++) {
                Time time1 = arrival_times.get(i);
                Time time2 = arrival_times.get(j);
                if (time1.hour <= time2.hour && time1.minute <= time2.minute && time1.second < time2.second) {
                    mintime = time1;
                }
                /*
                else if (time1.hour == time2.hour)
                {
                    if(time1.minute < time2.minute)
                    {
                        //leave in position
                    }
                    else if(time1.minute == time2.minute)
                    {
                        if(time1.second < time2.second)
                        {
                            //leave in position
                        }
                        else if (time1.second == time2.second)
                        {
                            //leave in position
                        }
                        else
                        {
                            arrival_times.remove(i);
                            arrival_times.add(i, time2);
                            arrival_times.remove(j);
                            arrival_times.add(j, time1);
                        }
                    }
                    else
                    {
                        arrival_times.remove(i);
                        arrival_times.add(i, time2);
                        arrival_times.remove(j);
                        arrival_times.add(j, time1);
                    }
                }
                else
                {
                    //swap time one and time 2
                    arrival_times.remove(i);
                    arrival_times.add(i, time2);
                    arrival_times.remove(j);
                    arrival_times.add(j, time1);
                }
            }

                 */
            }
            if(startOfUnsorted < arrival_times.size())
            {
                arrival_times.remove(startOfUnsorted);
                arrival_times.add(startOfUnsorted, mintime);
            }
            //else
            //{
             //   arrival_times.add(mintime);
            //}
            startOfUnsorted++;
        }
    }
}

