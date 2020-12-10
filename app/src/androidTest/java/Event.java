import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Event {
    private String eventId;
    private String name;
    private GeoPoint location;
    private Date date;
    //private ArrayList<Seat>;

    public Event(String eventId, String name, Date, GeoPoint location) {
        this.eventId = eventId;
        this.name = name;
        this.location = location;
        this.date = Date;
    }
    // GETTERS
    public String getID(){
        return eventId;
    }
    public String getName(){
        return name;
    }
    public GeoPoint getLocation(){
        return location;
    }
    public Date getDate(){
        return date;
    }


}
