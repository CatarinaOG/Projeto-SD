import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class Flight {

    private int occupancy;
    private Map<String,Booking> bookings;

    public Flight(){
        this.occupancy = 0;
        this.bookings = new HashMap<>();
    }

    public Flight(Flight f){
        this.occupancy = f.getOccupancy();
        this.bookings = f.getBookings();
    }



    public List<Booking> getBookingsDay(){
        return bookings.values().stream().map(Booking::clone).collect(Collectors.toList());
    }

    /**
     *
     * @param bookingId
     * @return 0 -> se nao existe, 1 -> se existir
     */
    public int cancelBooking(String bookingId){
        if(this.bookings.remove(bookingId) != null){
            this.occupancy--;
            return 1;
        }
        return 0;
    }

    public Booking addBooking(String bookingID,LocalDate date, String origin,String destination, String userID){
        this.occupancy++;
        Booking b = new Booking(bookingID,date,origin,destination,userID);
        this.bookings.put(bookingID,b);
        return b.clone();
    }

    public boolean hasSeat(int capacity){
        return occupancy < capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public Map<String, Booking> getBookings() {
        Map<String, Booking> res = new HashMap<>();

        for (Booking b : this.bookings.values())
            res.put(b.getBookingId(),b.clone());

        return res;
    }

    public void setBookings(Map<String, Booking> bookings) {
        this.bookings = bookings;
    }
}
