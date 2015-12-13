

//To check the temperature on the server (measured by the sensor)
public class Loop extends Thread
{
	private boolean isloop;
	private int timestep;
	private TemperatureService theTemperatureService;
	private float  temperature; 
	
	/*Constructor of the previous class, receiving 2 parameters:
	-timestep representing the time separating each loop
	-theTemperatureService who permits to get the temperature from the BeagleBone
*/
	
	public Loop(int timestep, TemperatureService theTemperatureService)
	{
		this.isloop = true;
		this.timestep = timestep;
		this.theTemperatureService = theTemperatureService;
	
	}
	
	//At each loop, the following program checks that the current temperature hasn't reached a treashold level
	public void run()
	{
		while (isloop)
		{
			this.temperature = theTemperatureService.getTemperature();
			
			
			try { 
				Thread.sleep(this.timestep); 
			}
			catch (Exception e) {
				isloop = false;
			}
		}
	}
}
