import java.io.*;
import java.util.Scanner;

public class ClientHandler {

    private DataInputStream in;
    private DataOutputStream out;
    private Scanner systemIn = new Scanner(System.in);

    public ClientHandler(DataInputStream in, DataOutputStream out){
        this.in = in;
        this.out = out;
    }

    public void run() throws IOException {

        String[] opcoes = {"Iniciar Sessão Normal", "Iniciar Sessão Gestor", "Registar nova conta"};
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

        System.out.println("Insira o seu username");
        username = systemIn.nextLine();

        System.out.println("Insira a sua password");
        password = systemIn.nextLine();

        this.out.writeInt(2); // 2 -> tag de autenticação gestor
        this.out.writeUTF(username);
        this.out.writeUTF(password);
        this.out.flush();

        String response = this.in.readUTF();
        System.out.println("Response: " + response);

    }


    public void signIn() throws IOException {
        String username;
        String password;

        System.out.println("Insira o seu username");
        username = systemIn.nextLine();

        System.out.println("Insira a sua password");
        password = systemIn.nextLine();

        this.out.writeInt(3); // 2 -> tag de registo
        this.out.writeUTF(username);
        this.out.writeUTF(password);
        this.out.flush();

        String response = this.in.readUTF();
        System.out.println("Response: " + response);

    }







}
