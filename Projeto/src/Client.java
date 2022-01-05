import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket clientS = new Socket("localhost",12345);

        DataInputStream in = new DataInputStream(clientS.getInputStream());
        DataOutputStream out = new DataOutputStream(clientS.getOutputStream());

        ClientHandler clientHandler = new ClientHandler(in,out);
        clientHandler.run();

        clientS.close();



    }



}
