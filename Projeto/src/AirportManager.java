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

    /**
     * metodo para fazer login de um utilizador
     * @param username
     * @param password
     * @return
     */
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

    /**
     * Metodo para fazer login do admin
     * @param username
     * @param password
     * @return
     */
    public boolean loginGestor(String username, String password){
        l.lock();
        try {
            return username.equals(admin.getKey()) && password.equals(admin.getValue());
        }finally {
            l.unlock();
        }
    }

    /**
     * Registar um utilizador
     * @param username
     * @param password
     */
    public void addUser(String username, String password){

        l.lock();
        users.put(username,new User(username,password));
        l.unlock();
    }

    /**
     * Registar um admin
     * @param username
     * @param password
     */
    public void addAdmin(String username, String password){
        l.lock();
        this.admin = new AbstractMap.SimpleEntry<>(username, password);
        l.unlock();
    }

    /**
     * Resgistar uma nova rota, info sobre um voo (origem, destino, capacidade).
     * @param origin
     * @param destination
     * @param capacity
     * @return
     */
    public boolean addRoute(String origin, String destination, int capacity){

        l.lock();
        try {
            return (routes.add(new Route(origin, destination, capacity)));
        }finally {
            l.unlock();
        }
    }

    /**
     * Apagar reservas de um dia -> cancelar um dia.
     * @param date
     */
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

    /**
     *
     * @param cities
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean bookFlight(List<String> cities, LocalDate startDate, LocalDate endDate){

        int currentCity = 0;
        for(int i = 0; i < cities.size()-1 ; i++){

            //this.routes.stream().

        }


        return true;
    }

    /**
     *
     */
    public void nextDay(){
        this.day = this.day.plusDays(1);
    }


    /**
     * Metodo que envia a listagem das rotas disponiveis para voos
     * @param out
     */
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

    /**
     *
     * @param bookingId
     * @return 0 se nao existir , 1 se existir
     */
    public int cancelBooking(String bookingId){
        this.l.lock();
        int res = 0;
        for(Route r : this.routes){
            r.lock(); //obter lock de todas as routes
        }
        this.l.unlock(); //libertar o lock da classe airportManager

        for (Route r : this.routes){
            res = r.cancelBooking(bookingId); //cancelar a reserva se existir nesta rota
            r.unlock(); //libertar o lock de uma route -> opde ser adquirido por outra thread
        }

        return res; //
    }

}