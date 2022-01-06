import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Flight {

    private int occupation;
    private Map<Integer,Booking> bookings;

    public Flight(){
        this.occupation = 0;
        this.bookings = new HashMap<>();
    }


    public List<Booking> getBookingsDay(){
        return bookings.values().stream().map(Booking::clone).collect(Collectors.toList());
    }


}
