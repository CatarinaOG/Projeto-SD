import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
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
    public boolean addUser(String username, String password){
        boolean ret = true;
        l.lock();
        if(!users.containsKey(username)) users.put(username,new User(username,password));
        else ret = false;
        l.unlock();
        return ret;
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

        this.l.lock();
        try {
            return (this.routes.add(new Route(origin, destination, capacity)));
        }finally {
            this.l.unlock();
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
            this.users.get(b.getUsername()).deleteBooking(idBooking);
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
    public Map.Entry<LocalDate, String> bookFlight(List<String> cities, LocalDate startDate, LocalDate endDate, String userID){

        List<Route> usedRoutes = new ArrayList<>();

        boolean compatible;
        this.l.lock();
        for(int i = 0; i < cities.size()-1 ; i++) {
            compatible = false;
            for (Route r : routes) {
                if (r.isCompatible(cities.get(i), cities.get(i+1))) {
                    compatible = true;
                    usedRoutes.add(r);
                    r.lock(); //adquirir o lock da route onde se vai alterar coisas
                }
            }
            if (!compatible){
                for(Route r : usedRoutes) r.unlock(); //libertar os locks das routes
                this.l.unlock(); // libertar o lock da airportManager
                return new AbstractMap.SimpleEntry<>(null, "1");
            }
        }

        this.l.unlock(); // libertar o lock do airportManager

        boolean solution = true;
        LocalDate sol = null;
        for( LocalDate d = startDate ; d.isBefore(endDate) ; d = d.plusDays(1) ){
            solution = true;

            for( Route r : usedRoutes ){
                if( !r.hasFlight(d) ) r.createFlight(d);
                else if (!r.hasSeat(d) && r.isNull(d)) solution = false;
            }

            if( solution ) {
                sol = d;
                break;
            }
        }

        String bookingID;
        Booking b;

        if( !solution ){
            for(Route r : usedRoutes) r.unlock(); //libertar os locks das routes
            return new AbstractMap.SimpleEntry<>(null, "2");
        }
        else {

            bookingID = UUID.randomUUID().toString();
            for (Route r : usedRoutes) {
                b = r.bookFlight(bookingID, sol, userID);

                User u = users.get(b.getUsername());
                u.addBooking(b);
                users.put(b.getBookingId(),u);

                r.unlock();
            }

        }

        return new AbstractMap.SimpleEntry<>(sol, bookingID);
    }

    public List<String> sendListAllFlights() {
        this.l.lock();                       // obter o lock do airport para certificar que nenhuma operação altera o estado das routes antes de as bloquear

        List<String> list = new ArrayList<>();

        for (Route r : this.routes) r.lock(); // obter o lock de todas as routes
        this.l.unlock();                     // libertar o lock do airport
        for (Route r : this.routes) {
            for (String s : r.serializeRoute())
                list.add(s);
            r.unlock();                      // libertar o lock da route depois de a analisar
        }

        return list;
    }

    /**
     *
     * @param bookingId
     * @return 0 se nao existir , 1 se existir
     */
    public boolean cancelBooking(String bookingId, String username){
        this.l.lock();
        boolean res = false;

        User u = this.users.get(username);

        if(!u.deleteBooking(bookingId)){
            this.l.unlock();
            return false;
        }
        this.users.put(u.getUsername(), u);


        for(Route r : this.routes){
            r.lock(); //obter lock de todas as routes
        }
        this.l.unlock(); //libertar o lock da classe airportManager

        for (Route r : this.routes){
            if(r.cancelBooking(bookingId)) res = true; //cancelar a reserva se existir nesta rota
            r.unlock(); //libertar o lock de uma route -> opde ser adquirido por outra thread
        }

        return res; //
    }

    
    public List<List<Route>> allPossibleFlights(String origin, String destination){
        
        List<String> citiesVisited = new ArrayList<>();
        List<List<Route>> possibleRoutes = new ArrayList<>();

        for(Route r : this.routes){
            // Se a rota analisada tiver início em A, considerar. Senão, passar à frente
            if(r.getOrigin().equals(origin)){ // se for uma rota de A para X
                if(r.getDestination().equals(destination)){ // Se X for B, a rota é direta
                    List<Route> direct_route = new ArrayList<>();
                    direct_route.add(r);
                    possibleRoutes.add(direct_route);
                    System.out.println("Found a direct route: " + r.getOrigin() + " -> " + r.getDestination());
                }
                else{ // Senão, vamos a analisar rotas do tipo X para Y (ou seja, com início em X) -> r2.getOrigin().equals(r.getDestination())
                    for(Route r2 : this.routes){  // de todas as rotas, vamos ver se Y é destino ou não
                        if(r2.getOrigin().equals(r.getDestination()) && r2.getDestination().equals(destination)){ //Se esse Y for igual a B
                            List<Route> l = new ArrayList<>();
                            l.add(r);
                            l.add(r2);
                            possibleRoutes.add(l);
                            System.out.println("Found a route with 2 flights: " + r.getOrigin() + " -> " + r.getDestination() +
                                    " / " + r2.getOrigin() + " -> " + r2.getDestination());
                        }
                        else{ // se Y não for B então vamos às rotas tentar encontrar rotas de Y para B (ou seja, com início em Y) -> r3.getOrigin().equals(r2.getDestination())
                            for(Route r3 : this.routes){ // rotas de Y para Z
                                if(r3.getOrigin().equals(r2.getDestination()) && r2.getOrigin().equals(r.getDestination()) && r3.getDestination().equals(destination)){ // se este
                                    List<Route> l = new ArrayList<>();
                                    l.add(r);
                                    l.add(r2);
                                    l.add(r3);
                                    possibleRoutes.add(l);
                                    System.out.println("Found a route with 3 flights: " + r.getOrigin() + " -> " + r.getDestination() +
                                                        " / " + r2.getOrigin() + " -> " + r2.getDestination() +
                                                        " / " + r3.getOrigin() + " -> " + r3.getDestination());
                                }
                            }
                        }
                    }
                }
            }
        }
        return possibleRoutes;
    }

}