import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Route {

    private String origin;
    private String destination;
    private int capacity;
    private Map<LocalDate,Flight> flights;


    public Route(String origin, String destination, int capacity){
        this.origin = origin;
        this.destination = destination;
        this.capacity = capacity;
        this.flights = new HashMap<>();
    }

    public Route(Route f){
        this.origin = f.getOrigin();
        this.destination = f.getDestination();
        this.capacity = f.getCapacity();
    }


    public List<Booking> deleteBookings(LocalDate date){
        List<Booking> list = this.flights.get(date).getBookingsDay();
        this.flights.put(date, null);
        return list;
    }

    public Route copyFlight(){
        return new Route(this.origin, this.destination, this.capacity);
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String toString() {
        return "Flight{" +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
