//Check by asking the temperature to the server
public class ClientLoop extends Thread
{
	private int timestep;
	private Client client;
	private boolean iflooping;

	
	//The constructor of the previous class
	public ClientLoop(int timestep, Client client)
	{
		this.iflooping = true;
		this.timestep = timestep;
		this.client = client;
	}
	
	//At each loop, the following method gets the temperature from the server and stores it in an array

	public void run()
	{
		Requests c;
		while (iflooping)
		{
			try {
				c = client.request("getTemp");	
				client.getClientGUI().displayComponent("Temperature", c);
			
				if (client.getSizeHistorical() == 10)
					this.client.getHistorical().remove(0);	
				
				this.client.getHistorical().add(c.getSample());
				Thread.sleep(this.timestep);
				
			} catch (Exception e) {
				this.iflooping = false;
			}
		}
	}
	
	//Permits to stop the loop if needed

	public void stopLoop()
	{
		this.iflooping = false;
	}
}
