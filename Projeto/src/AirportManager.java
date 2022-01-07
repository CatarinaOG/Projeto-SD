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
    public String bookFlight(List<String> cities, LocalDate startDate, LocalDate endDate, String userID){

        List<Route> usedRoutes = new ArrayList<>();

        boolean compatible;

        for(int i = 0; i < cities.size()-1 ; i++) {
            compatible = false;
            for (Route r : routes) {
                if (r.isCompatible(cities.get(i), cities.get(i+1))) {
                    compatible = true;
                    usedRoutes.add(r);
                }
            }
            if (!compatible) return "1";
        }


        boolean solution = true;
        LocalDate sol = null;
        for( LocalDate d = startDate ; d.isBefore(endDate) ; d = d.plusDays(1) ){
            solution = true;

            for( Route r : usedRoutes ){
                if( !r.hasFlight(d) ) r.createFlight(d);
                else if (!r.hasSeat(d)) solution = false;
            }

            if( solution ) {
                sol = d;
                break;
            }
        }

        String bookingID;

        if( !solution ) return "2";
        else {
            bookingID = UUID.randomUUID().toString();
            for (Route r : usedRoutes)
                r.bookFlight(bookingID, sol, userID);
        }

        return bookingID;
    }

    /**
     * Metodo que envia a listagem das rotas disponiveis para voos
     * @param out
     */
    public void sendListAllFlights(DataOutputStream out){
        int n_flights = this.routes.size();
        try {
            this.l.lock();                       // obter o lock do airport para certificar que nenhuma operação altera o estado das routes antes de as bloquear
            out.writeInt(n_flights);
            for(Route r : this.routes) r.lock(); // obter o lock de todas as routes
            this.l.unlock();                     // libertar o lock do airport
            for(Route r : this.routes){
                r.serializeRoute(out);
                r.unlock();                      // libertar o lock da route depois de a analisar
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

    /**
     *
     */
    public void nextDay(){
        this.day = this.day.plusDays(1);
    }

}