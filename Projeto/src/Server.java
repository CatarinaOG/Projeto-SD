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

        try {

            int option = in.readInt();
            while(option != 0) {

                switch (option) {
                    case 1: login(); break;
                    case 2: loginGestor(); break;
                    case 3: signIn(); break;
                    case 4: addRoute(); break;
                    case 5: deleteBookingsDay(); break;
                    case 6: bookFlight(); break;
                    case 7: listAllFlights(); break;
                    case 8: cancelBooking(); break;
                    default: break;
                }
                option = in.readInt();
            }
        } catch(Exception e){
            System.out.println("Client Exited");
        }

    }

    public void login() {
        try{
            String username = in.readUTF();
            String password = in.readUTF();

            if (manager.login(username, password)) out.writeUTF("Access legally accepted");
            else out.writeUTF("Access accepted");
            out.flush();
        }catch(IOException e){
            System.out.println("Error logging In");
        }


    }

    public void loginGestor() throws IOException {

        try {

            String username = in.readUTF();
            String password = in.readUTF();

            if (manager.loginGestor(username, password)) out.writeUTF("Acess accepted");
            else out.writeUTF("Acess denied");
            out.flush();
        }catch(IOException e) {
            System.out.println("Error loging in");
        }

    }

    public void signIn(){

        try {

            String username = in.readUTF();
            String password = in.readUTF();

            manager.addUser(username, password);
            out.writeUTF("Sign in with sucess");
            out.flush();

        }catch( IOException e){
            System.out.println("Error signing in");
        }

    }

    public void addRoute(){

        try {

            String origin = in.readUTF();
            String destination = in.readUTF();
            int capacity = in.readInt();

            if (manager.addRoute(origin, destination, capacity)) out.writeUTF("Route created!");
            else out.writeUTF("Route creation failed!");
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

    public void bookFlight(){

        try {

            int nrScales = in.readInt();
            List<String> cities = new ArrayList<>();

            for( int i = 0 ; i < (nrScales+2); i++ ) {
                cities.add(in.readUTF());
            }

            LocalDate startDate = LocalDate.parse(in.readUTF());
            LocalDate endDate = LocalDate.parse(in.readUTF());


            if (manager.bookFlight(cities, startDate, endDate)) out.writeUTF("Flight booked!");
            else out.writeUTF("Flight impossible to book!");
            out.flush();

        }catch( IOException e){
            System.out.println("Error booking flight");
        }


    }

    public void listAllFlights(){
        this.manager.sendListAllFlights(this.out);
    }

    public void cancelBooking(){
        try {

            String bookingId = in.readUTF(); //ler o id da reserva
            int existe = this.manager.cancelBooking(bookingId);
            if(existe == 1) out.writeUTF("Reserva " + bookingId + "cancelada com sucesso!");
            else out.writeUTF("Rserva com ID " + bookingId + " não existe!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


