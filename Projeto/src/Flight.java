import java.time.LocalDate;

public class Flight {

    private String id;
    private String origin;
    private String destination;
    LocalDate date;
    int capacity;
    int ocupation;


    public Flight(String id, String origin, String destination, LocalDate date, int capacity, int ocupation){
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.capacity = capacity;
        this.ocupation = ocupation;
    }

    public Flight(Flight f){
        this.id = f.getId();
        this.origin = f.getOrigin();
        this.destination = f.getDestination();
        this.date = f.getDate();
        this.capacity = f.getCapacity();
        this.ocupation = f.getOcupation();
    }

    public Flight copyFlight(String id, LocalDate date){
        return new Flight(id,this.origin,this.destination,date,this.capacity,0);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOcupation() {
        return ocupation;
    }

    public void setOcupation(int ocupation) {
        this.ocupation = ocupation;
    }


    public String toString() {
        return "Flight{" +
                "id='" + id + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", date=" + date +
                ", capacity=" + capacity +
                ", ocupation=" + ocupation +
                '}';
    }
}
