import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirportManager {
    private Map<LocalDate,Map<String,Flight>> flights;
    private Map<String, User> users;
    private List<Flight> commonFlights;
    private Map.Entry<String,String> admin;
    LocalDate day;

    public AirportManager(){
        this.flights = new HashMap<>();
        this.users = new HashMap<>();
    }

    public boolean login(String username, String password ){

        User u;
        if( (u = users.get(username)) != null )
            if( u.getUsername().equals(username) && u.getPassword().equals(password)) return true;

        return false;
    }

    public boolean loginGestor(String username, String password){
        return username.equals(admin.getKey()) && password.equals(admin.getValue());
    }

    public void addUser(String username, String password){
        users.put(username,new User(username,password));
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