// The Date Time Service Class - Written by Derek Molloy for the EE402 Module

import java.util.Calendar;
import java.util.Date;

//TimeDateService class permits to get the date of the different samples

public class TimeDateService
{
   //method returns date/time as a formatted String object
   public Date getDateAndTime()
   {
	 Date d = Calendar.getInstance().getTime();
	 return d;	
   }	
}
