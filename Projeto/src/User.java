import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class User {

    private String username;
    private String password;
    private Map<String, Booking > bookings;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.bookings = new HashMap<>();
    }


    public void deleteBooking(String idBooking){
        this.bookings.remove(idBooking);
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

    public Map<String, Booking> getBookings() {
        return this.bookings;
    }

    public void setBookings(Map<String, Booking> bookings) {
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
