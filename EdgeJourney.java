import java.util.ArrayList;

public class EdgeJourney {
    int from_stop_id;
    int to_stop_id;
    int transfer_type;
    //ArrayList<Integer> journey_ids = new ArrayList<>();
    double edgeWeight;
    double distance = -1;

    EdgeJourney(int from_stop_id, int to_stop_id, int transfer_type, double weight)
    {
        this.from_stop_id = from_stop_id;
        this.to_stop_id = to_stop_id;
        //this.time = time;
        this.transfer_type = transfer_type;
        this.edgeWeight = weight;
    }

    /*public void addJourneyID(int journeyID)
    {
        journey_ids.add(journeyID);
    }

    /*public void addJourneyTime(Time time)
    {
        this.time = time;
    }
     */

    /*public double getEdgeWeight()
    {
        return edgeWeight;
    }
     */

    public void addDistance(double distance)
    {
        this.distance = distance;
    }
}
