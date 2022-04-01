public class BusStop {
        int stop_id;
        double stop_lat;
        double stop_lon;
        String stop_name;

        BusStop(int stop_id, double stop_lat, double stop_lon, String stop_name)
        {
            this.stop_id = stop_id;
            this.stop_lat = stop_lat;
            this.stop_lon = stop_lon;
            this.stop_name = stop_name;

        }

        private void setStop_id(int stop_id)
        {
            this.stop_id = stop_id;
        }

        public int getStop_id()
        {
            return stop_id;
        }

        private void setStop_lat(double stop_lat)
        {
            this.stop_lat = stop_lat;
        }

        public double getStop_lat()
        {
            return stop_lat;
        }

        private void setStop_lon(double stop_lon)
        {
            this.stop_lon = stop_lon;
        }

        public double getStop_lon()
        {
            return stop_lon;
        }

        private void setStop_name(String stop_name)
        {
            this.stop_name = stop_name;
        }

        public String getStop_name()
        {
            return stop_name;
        }

        public void tellMeAboutThisBusStop()
        {
            System.out.println("Stop name : " + stop_name + '\n' + "Stop id : " + stop_id + '\n' + "Stop co-orddinates : ("
                    + stop_lat + ", " + stop_lon + ")" + '\n');
        }
}
