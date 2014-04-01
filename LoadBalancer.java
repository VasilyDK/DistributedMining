import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LoadBalancer {
	
	static int MAX_SLAVES = 100;
	
    public static void main(String[] args) throws IOException {

	    if (args.length != 2) {
	        System.err.println("Usage: java LoadBalancer <client port> <slave port>");
	        System.exit(1);
	    }
	    
	    BlockingQueue<SlaveInfo> slaveQueue = new ArrayBlockingQueue<SlaveInfo>(MAX_SLAVES);
    	
	    int slavePort = Integer.parseInt(args[1]);
        System.out.println("Waiting for Slaves...");
	    new SlaveListener(slavePort, slaveQueue).start();
	    
        int clientPort = Integer.parseInt(args[0]);
        boolean listening = true;
        System.out.println("Waiting for Clients...");
        try (ServerSocket serverSocket = new ServerSocket(clientPort)) {
            while (listening) {
                SlaveInfo nextSlave = slaveQueue.poll();
                
                Socket socket = serverSocket.accept();
                System.out.println("New Client Connection");
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(nextSlave);
                oos.flush();
                socket.close();             
                
                slaveQueue.add(nextSlave);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + clientPort);
            System.exit(-1);
        }
    }
}
