import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
	
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
            System.err.println(
                "Usage: java Client <host name> <port number> <mining string>");
            System.exit(1);
        }
		
		String host = args[0];
        int port = Integer.parseInt(args[1]);
        String miningString = args[2];
        StringContainer cont = new StringContainer(miningString);
		
		try {
            Socket socket = new Socket(host, port);
            System.out.println("Connection Established");
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(cont);
            oos.flush();
            
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            StringContainer result = (StringContainer)ois.readObject();
            
            System.out.println(result.GetString());
            
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                host);
            System.exit(1);
        } catch (ClassNotFoundException e) {
        	System.err.println("Class error");
            System.exit(1);
		}
	}
}
