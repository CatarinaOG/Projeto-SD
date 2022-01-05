import java.awt.print.Book;
import java.time.LocalDate;

public class Booking {

    private String bookingId;
    private LocalDate flightDate;
    private String origin;
    private String destination;
    private String userId;

    public Booking(String bookingId, LocalDate flightDate, String origin, String destination, String userId){
        this.bookingId = bookingId;
        this.flightDate = flightDate;
        this.origin = origin;
        this.destination = destination;
        this.userId = userId;
    }

    public Booking(Booking b){
        this.userId = b.getUserId();
        this.flightDate = b.getFlightDate();
        this.origin = b.getOrigin();
        this.destination = b.getDestination();
        this.userId = b.getUserId();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Booking clone(){
        return new Booking(this);
    }

}
