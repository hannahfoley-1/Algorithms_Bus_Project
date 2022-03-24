import java.util.ArrayList;

public class EdgeJourney {
    int from_stop_id;
    BusStop from_stop;
    int to_stop_id;
    BusStop to_stop;
    Time time;
    int transfer_type;
    ArrayList<Integer> journey_ids = new ArrayList<>();
    double edgeWeight;

    EdgeJourney(int from_stop_id, int to_stop_id, int transfer_type, double weight)
    {
        this.from_stop_id = from_stop_id;
        this.to_stop_id = to_stop_id;
        //this.time = time;
        this.transfer_type = transfer_type;
        this.edgeWeight = weight;
    }

    public void addJourneyID(int journeyID)
    {
        journey_ids.add(journeyID);
    }

    public void addJourneyTime(Time time)
    {
        this.time = time;
    }



}

