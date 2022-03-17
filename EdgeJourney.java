public class EdgeJourney {
        int from_stop_id;
        BusStop from_stop;
        int to_stop_id;
        BusStop to_stop;
        Time time;

        EdgeJourney(int from_stop_id, int to_stop_id, Time time)
        {
            this.from_stop_id = from_stop_id;
            this.to_stop_id = to_stop_id;
            this.time = time;
        }

}
