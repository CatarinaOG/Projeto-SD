import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AirportManager {
    private Map<LocalDate,Map<String,Flight>> flights;
    private Map<Integer, User> users;
    LocalDate day;

    public AirportManager(){
        this.flights = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void addFlight(Flight f){
        LocalDate date = LocalDate.now();

        for (int i = 0; i < 30; i++, date = date.plusDays(1)){
            if (!this.flights.containsKey(date)){
                Map<String, Flight> insert = new HashMap<>();
                insert.put(f.getId(), f);
                this.flights.put(date, insert);
            }
            else{
                Map<String, Flight> update = this.flights.get(date);
                update.put(f.getId(), f);
                this.flights.put(date, update);
            }
        }
    }



    public void nextDay(){
        this.day = this.day.plusDays(1);
    }



}