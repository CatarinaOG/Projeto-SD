import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AirportManager {

    private Lock l = new ReentrantLock();
    private Map<String, User> users;
    private List<Route> routes;

    private LocalDate day;
    private Map.Entry<String,String> admin;


    public AirportManager(){
        this.users = new HashMap<>();
        this.routes = new ArrayList<>();
    }

    public boolean login(String username, String password ){

        User u;
        try {
            l.lock();
            if ((u = users.get(username)) != null)
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) return true;
        }finally {
            l.unlock();
        }

        return false;
    }

    public boolean loginGestor(String username, String password){
        l.lock();
        try {
            return username.equals(admin.getKey()) && password.equals(admin.getValue());
        }finally {
            l.unlock();
        }
    }

    public void addUser(String username, String password){

        l.lock();
        users.put(username,new User(username,password));
        l.unlock();
    }

    public void addAdmin(String username, String password){
        l.lock();
        this.admin = new AbstractMap.SimpleEntry<>(username, password);
        l.unlock();
    }

    public boolean addRoute(String origin, String destination, int capacity){

        l.lock();
        try {
            return (routes.add(new Route(origin, destination, capacity)));
        }finally {
            l.unlock();
        }
    }

    public void deleteBookings(LocalDate date){
        List<Booking> bookingsDeleted = new ArrayList<>();

        this.l.lock();

        for(Route r : this.routes){
            r.deleteBookings(date).stream().map(bookingsDeleted::add);
        }

        for( Booking b : bookingsDeleted ){
            String idBooking = b.getBookingId();
            this.users.get(b.getUserId()).deleteBooking(idBooking);
        }

        this.l.unlock();
    }

    public boolean bookFlight(List<String> cities, LocalDate startDate, LocalDate endDate){

        int currentCity = 0;
        for(int i = 0; i < cities.size()-1 ; i++){

            //this.routes.stream().

        }


        return true;
    }

    public void nextDay(){
        this.day = this.day.plusDays(1);
    }


    public void sendListAllFlights(DataOutputStream out){
        int n_flights = this.routes.size();
        try {
            out.writeInt(n_flights);
            for(Route r : this.routes){
                r.serializeRoute(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}