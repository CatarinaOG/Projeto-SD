import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class ClientHandler {

    private DataInputStream in;
    private DataOutputStream out;
    private Scanner systemIn = new Scanner(System.in);

    public ClientHandler(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void run() throws IOException {

        String[] opcoes = {"User Log in", "Admin Log in", "Sign up"};
        Menu menu = new Menu("Airport Menu", opcoes);

        menu.setHandler(1, this::loginNormal);
        menu.setHandler(2, this::loginGestor);
        menu.setHandler(3, this::signIn);
        menu.run();

    }

    public void loginNormal() throws IOException {

        String username;
        String password;

        System.out.println("Insira o seu username");
        username = systemIn.nextLine();

        System.out.println("Insira a sua password");
        password = systemIn.nextLine();

        this.out.writeInt(1); // 1 -> tag de autenticação normal
        this.out.writeUTF(username);
        this.out.writeUTF(password);
        this.out.flush();

        if(this.in.readBoolean()){
            System.out.println("Access granted!");
            String[] options = {"Book a Flight","List all flights", "Cancel Booking"};
            Menu usersMenu = new Menu("User's Menu", options);

            usersMenu.setHandler(1,this::bookAFlight);
            usersMenu.setHandler(2, this::listAllFlights);
            usersMenu.setHandler(3, this::cancelBooking);

            usersMenu.run();
        }
        else System.out.println("Access denied!");

    }

    public void loginGestor() throws IOException {

        String username;
        String password;

        System.out.println("Insert Username");
        username = systemIn.nextLine();

        System.out.println("Insert Password");
        password = systemIn.nextLine();

        this.out.writeInt(2); // 2 -> tag de autenticação gestor
        this.out.writeUTF(username);
        this.out.writeUTF(password);
        this.out.flush();

        if(this.in.readBoolean()){
            System.out.println("Access granted!");
            String[] opcoes = {"Add route", "Delete bookings in a specific day"};
            Menu menuGestor = new Menu("Admin Menu", opcoes);

            menuGestor.setHandler(1, this::addRoute);
            menuGestor.setHandler(2, this::deleteBookingsDay);

            menuGestor.run();
        }
        else System.out.println("Access denied!");



    }

    public void signIn() throws IOException {
        String username;
        String password;

        System.out.println("Insert Username");
        username = systemIn.nextLine();

        System.out.println("Insert Password");
        password = systemIn.nextLine();

        this.out.writeInt(3); // 3 -> tag de registo
        this.out.writeUTF(username);
        this.out.writeUTF(password);
        this.out.flush();

        if(this.in.readBoolean()) System.out.println("Sign Up succeeded!");

    }

    public void addRoute() throws IOException {

        System.out.println("Insert the origin of the route:");
        String origin = systemIn.nextLine();

        System.out.println("Insert the destination of the route:");
        String destination = systemIn.nextLine();

        System.out.println("Insert the capacity of the flight:");
        int capacity = Integer.parseInt(systemIn.nextLine());

        this.out.writeInt(4); // 4 -> add route
        this.out.writeUTF(origin);
        this.out.writeUTF(destination);
        this.out.writeInt(capacity);
        this.out.flush();

        String response = this.in.readUTF();
        System.out.println("Response: " + response);

    }

    public void deleteBookingsDay() throws IOException {

        this.out.writeInt(5);

        System.out.println("Insert the date of the day you want to delete (Year-Month-Day)");
        this.out.writeUTF(systemIn.nextLine());
        out.flush();

    }

    public void bookAFlight() throws IOException {

        this.out.writeInt(6);

        System.out.println("How many scales are needed?");
        int nrScales = Integer.parseInt(systemIn.nextLine());
        this.out.writeInt(nrScales);

        for( int i = 0 ; i< (nrScales+2) ; i++ ){
            System.out.println("Insert city " + (i+1));
            this.out.writeUTF(systemIn.nextLine());

        }

        System.out.println("Insert the date of the day to start the search (Year-Month-Day)");
        this.out.writeUTF(systemIn.nextLine());

        System.out.println("Insert the date of the day to end the search (Year-Month-Day)");
        this.out.writeUTF(systemIn.nextLine());

        this.out.flush();

        String response = this.in.readUTF();
        System.out.println("Response: " + response);


    }

    public void listAllFlights(){
        try{
            this.out.writeInt(7);

            int n_flights = this.in.readInt();
            System.out.println("There are " + n_flights + " flights available!");

            for(int i = 0; i < n_flights; i++){
                System.out.println(this.in.readUTF() + " -> " + this.in.readUTF() + " (Capacity: " + this.in.readInt() + ")");
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cancelBooking(){
        System.out.print("Insert the booking ID: ");
        String bookingId = this.systemIn.nextLine();

        try {
            this.out.writeInt(8);
            this.out.writeUTF(bookingId);

            if(this.in.readBoolean()) System.out.println("Reserva " + bookingId + " cancelada com sucesso!");
            else System.out.println("Reserva com ID " + bookingId + " não existe!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
