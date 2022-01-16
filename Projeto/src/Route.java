import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Route {

    private String origin;
    private String destination;
    private int capacity;
    private Lock l;
    private Map<LocalDate,Flight> flights;


    public Route(String origin, String destination, int capacity){
        this.origin = origin;
        this.destination = destination;
        this.capacity = capacity;
        this.flights = new HashMap<>();
        this.l = new ReentrantLock(); //cada instancia de route tem um lock
    }

    public Route(Route f){
        this.origin = f.getOrigin();
        this.destination = f.getDestination();
        this.capacity = f.getCapacity();
        this.flights = f.getFlights();
    }


    public List<Booking> deleteBookings(LocalDate date){
        List<Booking> list = null;
        Flight aux = this.flights.get(date);
        if(aux != null) list =  aux.getBookingsDay();
        this.flights.put(date, null);
        return list;
    }

    public List<String> serializeRoute(){
        
        List<String> list = new ArrayList<>();
        
        list.add(this.origin);
        list.add(this.destination);
        list.add(String.valueOf(this.capacity));

        return list;
    }

    public void lock(){
        this.l.lock();
    }

    public void unlock(){
        this.l.unlock();
    }

    public boolean cancelBooking(String bookingId){
        for(Flight f : this.flights.values())
            if(f != null && f.cancelBooking(bookingId) == 1) return true;

        return false;
    }

    public boolean isCompatible(String origin, String destination){
        return origin.equals(this.origin) && destination.equals(this.destination);
    }

    public boolean hasFlight(LocalDate date){
        return flights.containsKey(date);
    }

    public void createFlight(LocalDate date){
        flights.put(date,new Flight());
    }

    public Booking bookFlight(String bookingID, LocalDate date,String userID){
        Flight f = this.flights.get(date);
        Booking b = f.addBooking(bookingID, date, origin, destination, userID);
        this.flights.put(date,f);
        return b;
    }

    public boolean hasSeat(LocalDate date){
        Flight f = flights.get(date);
        if(f == null) return false;
        else return f.hasSeat(capacity);
    }

    public boolean isNull(LocalDate date){
        return flights.containsKey(date) && flights.get(date) == null;
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

    public Map<LocalDate, Flight> getFlights(){
        Map<LocalDate, Flight> res = new HashMap<>();
        for(Map.Entry<LocalDate,Flight> f : this.flights.entrySet()){
            if(f.getValue() != null)
                res.put(f.getKey(),f.getValue().clone());
        }
        return res;
    }

    public String toString() {
        return "Flight{" +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", capacity=" + capacity +
                '}';
    }

    public Route clone(){
        return new Route(this);
    }

}
