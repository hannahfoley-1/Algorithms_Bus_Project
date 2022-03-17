import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class BusStopsList {
    ArrayList<BusStop> list = new ArrayList<BusStop>();
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
                scanner.nextInt();
                String stop_name = scanner.next();
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
        sortArrayByStopID();
        return binarySearchByStopID(id);
    }

    public void sortArrayByStopID()
    {
        ArrayList<BusStop> copy = new ArrayList<>(list.size()-1);
        mergeSort(list, copy, 0, list.size()-1);
    }

    private static void mergeSort(ArrayList a, ArrayList copy, int lo, int hi)
    {
        if(hi <= lo)
        {
            return;
        }
        int mid = lo + (hi - lo)/2;
        //recursively sort LHS
        mergeSort(a, copy, lo, mid);
        //recursively sort RHS
        mergeSort(a, copy,mid+1, hi);
        //merge the sorted halves
        merge(a, copy, lo, mid, hi);
    }

    private static void merge(ArrayList<BusStop> a, ArrayList<BusStop> copy, int lo, int mid, int high)
    {
        for(int k = lo; k <= high; k++)
        {
            copy.add(k, a.get(k));
        }
        int i = lo;
        int j = mid + 1;

        for(int k = lo; k <= high; k++)
        {
            if(i > mid)
            {
                a.add(k, copy.get(j++));
            }
            else if(j > high)
            {
                a.add(k, copy.get(i++));
            }
            else if(a.get(j).stop_id < copy.get(i).stop_id)
            {
                a.add(k, copy.get(j++));
            }
            else
            {
                a.add(k, copy.get(i++));
            }
        }
    }

    public BusStop binarySearchByStopID(int stop_id)
    {
        int low = 0;
        int high = list.size();
        while(low <= high)
        {
            int mid = low + (high-low) / 2;
            if(stop_id < list.get(mid).stop_id)
            {
                high = mid -1;
            }
            else if (stop_id > list.get(mid).stop_id)
            {
                low = mid + 1;
            }
            else if(stop_id == list.get(mid).stop_id)
            {
                return list.get(mid);
            }
        }
        return null;
    }

}
