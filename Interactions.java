


import java.io.*;			
import java.util.Date;

// The Interactions class contains all the informations about the the temperature sample

public class Interactions implements Serializable 
{
	private float temperature;
	private Date date;
	private static int samplenum;
	
	static final long serialVersionUID = 1L;
	
	/*The constructor of the previous class
	  Increment the samplenum to keep track on the number of sample
	 */
	public Interactions(float temperature) 
	{
		this.temperature = temperature; 
		samplenum++;
	}
	
	//To get the temperature and return it (as a float)
	public float getTemperature() 
	{
		return this.temperature;
	}
	
	//To set the temperature (for the Interface)
	public void setTemperature(float temp) 
	{
		this.temperature = temp;
	}
	
	//To get the actual date and return it
	public Date getDate() 
	{
		return this.date;
	}
	
	// To set the date (time also)

	public void setDate(Date d) 
	{
		this.date = d;
	}
	

	
	}
