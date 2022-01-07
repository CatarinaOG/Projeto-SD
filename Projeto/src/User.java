import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class User {

    private String username;
    private String password;
    private Map<String, List<Booking>> bookings;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.bookings = new HashMap<>();
    }

    public boolean deleteBooking(String idBooking){
        return (this.bookings.remove(idBooking) != null);
    }

    public void addBooking(Booking b){
        List<Booking> l;
        if(!this.bookings.containsKey(b.getBookingId())) l = new ArrayList<>();
        else l = this.bookings.get(b.getBookingId());

        l.add(b);
        this.bookings.put(b.getBookingId(), l);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, List<Booking>> getBookings() {
        return this.bookings;
    }

    public void setBookings(Map<String, List<Booking>> bookings) {
        this.bookings = bookings;
    }

    public String toString() {
        return "User{" +
                "username='" + this.username + '\'' +
                ", password='" + this.password + '\'' +
                ", bookings=" + this.bookings +
                '}';
    }
}
