import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Route {

    private String origin;
    private String destination;
    private int capacity;
    private Lock l = new ReentrantLock();
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

    public void serializeRoute(DataOutputStream out){
        try {
            out.writeUTF(this.origin);
            out.writeUTF(this.destination);
            out.writeInt(this.capacity);
        } catch (IOException e) {

        }
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

    public void lock(){
        this.l.lock();
    }

    public void unlock(){
        this.l.unlock();
    }

    public int cancelBooking(String bookingId){
        for(Flight f : this.flights.values()){
            if(f.cancelBooking(bookingId) == 1) return 1;
        }
        return 0;
    }
}
