import java.awt.*;

import javax.swing.*;

import java.util.*;

//This class draws a Interface from an Array of Samples

public class Interface extends JPanel 
{
	static final long serialVersionUID = 1L;

	private ArrayList<Interactions> array;
	private int space = 20;
	private int sidespace = 10;
	private int topspace = 30;

	//Constructor of the previous class
	public Interface(ArrayList<Interactions> array)
	{
		this.array = array;
	}



	//This method permits to paint the barChart in a JPanel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);


		int  x;
		int width;
		int height;
		float maxi = getValue(0);
		//Max Value
		for(int i=1; i<array.size(); i++)
			if(getValue(i)>maxi)
				maxi = getValue(i);

		width = ((getWidth()- (2*sidespace)) / array.size()) - space;
		x = sidespace;

		for(int j=0; j<array.size(); j++)
		{
			height = (int)((getHeight()-topspace) * ((double)Math.abs(getValue(j)) / maxi));
			
			//Write the strings
			g.setColor(Color.black);
			g.drawString( String.format("%.2f", getValue(j))+" ∞C", x+(width/2)-20, (getHeight()-height) - topspace/2);
			
			//Draw the rectangles
			if (getValue(j)>=0)
				g.setColor(Color.red);
			else g.setColor(Color.blue);
			g.fill3DRect(x, getHeight() - height, width, height - topspace, true);

			

			x += (width + space);
		}
		//Draw the date of the last value
		g.setFont(new Font("default", Font.PLAIN, 12));
		g.drawString("Last Sample : " + getTitle(array.size()-1), getWidth()/2 - 150, getHeight() - topspace/4);

	}

	// Get the different values
	private float getValue(int index)
	{
		return (array.get(index).getTemperature());
	}
	
	//Permit to convert the dates into string

	private String getTitle(int index)
	{
		return (""+array.get(index).getDate());
	}
	
	
}
