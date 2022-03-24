public class Time {
    int hour;
    int minute;
    int second;

    Time(int hour, int minute, int second)
    {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    Time GetTimeFromString(String time)
    {
        time = time.substring(1);
        //this line makes sure that we skip the space after the comma, otherwise the next line wont work
        String[] split = time.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);
        int seconds = Integer.parseInt(split[2]);
        if(hours > 23 || minutes > 59 || seconds > 59 || hours < 0 || minutes < 0 || seconds < 0)
        {
            return null;
        }
        Time timeObj = new Time(hours, minutes, seconds);
        return timeObj;
    }
}

