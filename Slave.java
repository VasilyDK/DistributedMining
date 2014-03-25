import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Slave {
    public static void main(String[] args) throws IOException {

	    if (args.length != 4) {
	        System.err.println("Usage: java KKMultiServer <LB host> <LB port> <client host> <client port>");
	        System.exit(1);
	    }
	    
	    String LBhost = args[0];
        int LBport = Integer.parseInt(args[1]);
        String clientHost = args[2];
        int clientPort = Integer.parseInt(args[3]);

	    try {        
            Socket socket = new Socket(LBhost, LBport);
            System.out.println("Connection Established");
            
            SlaveInfo sInfo = new SlaveInfo(clientHost, clientPort);
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(sInfo);
            oos.flush();
            
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + LBhost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                LBhost);
            System.exit(1);
        }
	    
	    boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(clientPort)) {
            while (listening) {
                System.out.println("New Client Connection");            
                new MiningThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + clientPort);
            System.exit(-1);
        }
    }
}