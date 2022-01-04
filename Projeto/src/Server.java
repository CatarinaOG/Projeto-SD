import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static void main (String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            AirportManager manager = new AirportManager();

            manager.addUser("eu","la");

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

    public ServerWorker (Socket socket, AirportManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    public void run() {

        try {

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String username;
            String password;


            int option = in.readInt();;
            while(option != 0) {

                switch (option) {

                    case 1: //-----------------------------Iniciar Sessao Normal----------------------------------------
                        username = in.readUTF();
                        password = in.readUTF();

                        if (manager.login(username, password)) out.writeUTF("Acesso legalmente aceite");
                        else out.writeUTF("Acesso negado");
                        out.flush();
                        break;


                    case 2://-----------------------------Iniciar Sessao Gestor-----------------------------------------
                        username = in.readUTF();
                        password = in.readUTF();

                        if (manager.loginGestor(username, password)) out.writeUTF("Acesso legalmente aceite");
                        else out.writeUTF("Acesso negado");
                        out.flush();
                        break;


                    case 3: //-----------------------------------Sign In------------------------------------------------
                        username = in.readUTF();
                        password = in.readUTF();

                        manager.addUser(username, password);
                        out.writeUTF("Sign in com sucesso");
                        out.flush();
                        break;

                    default:

                }
                option = in.readInt();
            }
        } catch(Exception e){
            System.out.println("Não haverá mais ações");
        }

    }
}


