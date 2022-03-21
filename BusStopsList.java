import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class BusStopsList {
    ArrayList<BusStop> list = new ArrayList<BusStop>();
    //Map<Integer, String> idAndNames;
    boolean valid = true;

    BusStopsList(String filename)
    {
        if (filename == null || filename == "") {
            valid = false;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
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
                list.add(currentStop);
                scanner.close();
                line = reader.readLine();
            }

        }
        catch (Exception e)
        {
            valid = false;
        }
    }

    public BusStop getBusStopByID(int id)
    {
        //Collections.sort(list, BusStop.BusStopIDComparator);
        //chaning over to an array cost the array list is causing problems not deleting what was already exisitng in the place im adding to
        BusStop[] busStopList = new BusStop[list.size()];
        for(int i = 0; i < list.size(); i++)
        {
            busStopList[i] = list.get(i);
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

        //for(int k = lo; k <= high; k++)
        //{
          //  copy[k] = a[k];
        //}

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

        /*
        while(i <= mid && j <= high)
        {
            if(copy[i].stop_id <= copy[j].stop_id)
            {
                a[k] = copy[i];
                i++;
            }
            else
            {
                a[k] = copy[j];
                j++;
            }
            k++;
        }
        while(i <= mid)
        {
            a[k] = copy[i];
            k++;
            i++;
        }

         */
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
                return list.get(mid);
            }
        }
        return null;
    }

    int size()
    {
        return list.size();
    }

}
