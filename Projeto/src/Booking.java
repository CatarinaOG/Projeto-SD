import java.awt.print.Book;
import java.time.LocalDate;

public class Booking {

    private String bookingId;
    private LocalDate flightDate;
    private String origin;
    private String destination;
    private String username;

    public Booking(String bookingId, LocalDate flightDate, String origin, String destination, String username){
        this.bookingId = bookingId;
        this.flightDate = flightDate;
        this.origin = origin;
        this.destination = destination;
        this.username = username;
    }

    public Booking(Booking b){
        this.bookingId = b.getBookingId();
        this.username = b.getUsername();
        this.flightDate = b.getFlightDate();
        this.origin = b.getOrigin();
        this.destination = b.getDestination();
        this.username = b.getUsername();
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public String getOrigin() {
        return origin;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", flightDate=" + flightDate +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public Booking clone(){
        return new Booking(this);
    }

}
