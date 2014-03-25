import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SlaveListener extends Thread {

	private int port;
	private BlockingQueue<SlaveInfo> queue;
	
	public SlaveListener(int port, BlockingQueue<SlaveInfo> queue)
	{
		this.port = port;
		this.queue = queue;
	}
	
	public void run() {
		boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (listening) {
                System.out.println("New Slave Connection");
                
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                SlaveInfo newSlave = (SlaveInfo)ois.readObject();
                socket.close();
                
                queue.add(newSlave);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        } catch (ClassNotFoundException e) {
        	System.err.println("Class error");
            System.exit(-1);
		}
    }
}
