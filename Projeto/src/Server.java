import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main (String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            AeroportManager manager;

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
    private AeroportManager manager;

    public ServerWorker (Socket socket, AeroportManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    public void run() {

        try {
            DataInputStream b = new DataInputStream(socket.getInputStream());
            //Contact c = Contact.deserialize(b);
            //manager.update(c);

        } catch(Exception e){
            e.printStackTrace();
        }

    }
}


class AeroportManager {
    private HashMap<String, Voo> flies;

    public ContactManager() {

    }


    public void update() {
    }



}