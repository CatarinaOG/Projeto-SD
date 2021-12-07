import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static void main (String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            AirportManager manager;

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
            DataInputStream b = new DataInputStream(socket.getInputStream());
            //Contact c = Contact.deserialize(b);
            //manager.update(c);

        } catch(Exception e){
            e.printStackTrace();
        }

    }
}


