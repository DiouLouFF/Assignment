import java.io.*;

// The following class is essential to the whole project: it permits to get the temperature from the server thanks to a sensor connected to the BBB

public class TemperatureService{ 
	
	private static String LDR_PATH="/sys/bus/iio/devices/iio:device0/in_voltage";
	private BufferedReader br;

	
	// We get the temperature from a file specified by LDR_PATH

	public float getTemperature() {
		int s = 0;
		
		float temperature=0.0f;
		try {
			br = new BufferedReader( new FileReader(LDR_PATH + s + "_raw"));
			String str;
			str = br.readLine();
			temperature=adcToCelcius(str);	
			System.out.println("Temperature is :" + temperature);
			
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException:" + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		return temperature;
	}
	
	//The following method converts the value return by the sensor to a value in Celsius degree

	private  float adcToCelcius(String str)
	{	
		int value;
		try {
			value = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
		
		float cur_voltage = value * (1.80f/4096.0f);
		float diff_degreesC = (cur_voltage-0.75f)/0.01f;
		return (25.0f + diff_degreesC);
	}
	
}
