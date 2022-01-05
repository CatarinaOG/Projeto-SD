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

        String response = this.in.readUTF();
        System.out.println("Response: " + response);

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

        String response = this.in.readUTF();
        System.out.println("Response: " + response);

        String[] opcoes = {"Add route", "Delete bookings in a specific day"};
        Menu menuGestor = new Menu("Admin Menu", opcoes);

        menuGestor.setHandler(1, this::addRoute);
        menuGestor.setHandler(2, this::deleteBookingsDay);

        menuGestor.run();

    }

    public void signIn() throws IOException {
        String username;
        String password;

        System.out.println("Insert Username");
        username = systemIn.nextLine();

        System.out.println("Insert Password");
        password = systemIn.nextLine();

        this.out.writeInt(3); // 2 -> tag de registo
        this.out.writeUTF(username);
        this.out.writeUTF(password);
        this.out.flush();

        String response = this.in.readUTF();
        System.out.println("Response: " + response);

    }

    public void addRoute() throws IOException {

        System.out.println("Insert origin of the route");
        String origin = systemIn.nextLine();

        System.out.println("Insert destination of the route");
        String destination = systemIn.nextLine();

        System.out.println("Insert destination of the route");
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

        System.out.println("Insert the date of the day you want to delete (Year-Month-Day)");
        this.out.writeUTF(systemIn.nextLine());

    }




}
