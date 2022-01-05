import java.util.*;
import java.util.stream.Collectors;

public class Flight {

    private int ocupacao;
    private Map<Integer,Booking> bookings;

    public Flight(){
        this.ocupacao = 0;
        this.bookings = new HashMap<>();
    }


    public List<Booking> getBookingsDay(){
        return bookings.values().stream().map(Booking::clone).collect(Collectors.toList());
    }


}
