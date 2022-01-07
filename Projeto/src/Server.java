import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    public static void main (String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            AirportManager manager = new AirportManager();

            manager.addUser("2","c");
            manager.addAdmin("1","g");

            while (true) {
                Socket socket = serverSocket.accept();
                Thread worker = new Thread(new ServerWorker(socket, manager));
                worker.start();
            }
        }catch(IOException e){
            System.out.println(e.getStackTrace());
        }
    }
}


class ServerWorker implements Runnable {
    private Socket socket;
    private AirportManager manager;
    private DataInputStream in;
    private DataOutputStream out;

    public ServerWorker (Socket socket, AirportManager manager) throws IOException {
        this.socket = socket;
        this.manager = manager;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        String username = null;
        try {

            int option = in.readInt();
            while(option != 0) {

                switch (option) {
                    case 1: username = login(); break;
                    case 2: loginGestor(); break;
                    case 3: signUp(); break;
                    case 4: addRoute(); break;
                    case 5: deleteBookingsDay(); break;
                    case 6: bookFlight(username); break;
                    case 7: listAllFlights(); break;
                    case 8: cancelBooking(username); break;
                    default: break;
                }
                option = in.readInt();
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Client Exited");
        }
    }

    public String login() {
        String username = null;
        try{
            username = in.readUTF();
            String password = in.readUTF();

            if (manager.login(username, password)) out.writeBoolean(true);
            else out.writeBoolean(false);
            out.flush();
        }catch(IOException e) {
            try{
                this.out.writeBoolean(false);
            }
            catch(IOException exp){
                exp.printStackTrace();
            }
        }
        return username;
    }

    public void loginGestor() throws IOException {

        try {

            String username = in.readUTF();
            String password = in.readUTF();

            if (manager.loginGestor(username, password)) out.writeBoolean(true);
            else out.writeBoolean(false);
            out.flush();
        }catch(IOException e) {
            try{
                this.out.writeBoolean(false);
            }
            catch(IOException exp){
                exp.printStackTrace();
            }
        }
    }

    public void signUp(){

        try {
            String username = in.readUTF();
            String password = in.readUTF();

            boolean success = manager.addUser(username, password);
            this.out.writeBoolean(success);
            out.flush();

        }catch( IOException e){
            try{
                this.out.writeBoolean(false);
            }
            catch(IOException exp){
                exp.printStackTrace();
            }
        }

    }

    public void addRoute(){

        try {

            String origin = in.readUTF();
            String destination = in.readUTF();
            int capacity = in.readInt();

            if (manager.addRoute(origin, destination, capacity)) out.writeBoolean(true);
            else out.writeBoolean(false);
            out.flush();

        }catch( IOException e){
            System.out.println("Error adding route");
        }
    }

    public void deleteBookingsDay(){
        try {

            LocalDate date = LocalDate.parse(in.readUTF());
            this.manager.deleteBookings(date);

            out.flush();

        }catch( IOException e){
            System.out.println("Error at deleting bookings in this day");
        }
    }

    public void bookFlight(String userID){
        try {
            int nrScales = in.readInt();
            List<String> cities = new ArrayList<>();

            for( int i = 0 ; i < (nrScales+2); i++ ) {
                cities.add(in.readUTF());
            }

            LocalDate startDate = LocalDate.parse(in.readUTF());
            LocalDate endDate = LocalDate.parse(in.readUTF());

            String bookingId;
            Map.Entry<LocalDate, String> r = manager.bookFlight(cities, startDate, endDate,userID);

            if(r.getValue().equals("1") || r.getValue().equals("2")) out.writeUTF(r.getValue());
            else{
                out.writeUTF(r.getValue());
                out.writeUTF(r.getKey().toString());
            }

            out.flush();

        }catch( IOException e){
            System.out.println("Error booking flight");
        }
    }

    public void listAllFlights(){

        List<String> list = this.manager.sendListAllFlights();

        try {
            out.writeInt(list.size()/3);

            for( int i=0 ; i<list.size() ; i+=3 ){

                out.writeUTF(list.get(i));
                out.writeUTF(list.get(i+1));
                out.writeInt(Integer.parseInt(list.get(i+2)));

            }

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void cancelBooking(String username){
        try {

            String bookingId = in.readUTF(); //ler o id da reserva
            out.writeBoolean(this.manager.cancelBooking(bookingId, username));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


