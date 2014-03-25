import java.io.Serializable;


@SuppressWarnings("serial")
public class SlaveInfo implements Serializable {

	SlaveInfo(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
	
	public String GetHost()
	{
		return host;
	}
	
	public int GetPort()
	{
		return port;
	}
	
	private String host;
	private int port;
}
