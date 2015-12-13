
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;


import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;

import javax.swing.event.ChangeListener;

import java.io.*;

//The ClientGUI class permits to the Client (the user) to communicate with the server (hosted on the BeagleBone)

public class ClientGUI extends JFrame implements ChangeListener, MouseListener,ActionListener
{
	static final long serialVersionUID = 1L;
	
	private Client client;
	private Interface chart;
	private JPanel graph, graphinput, settings,inputs, dashboard;
	
	private JButton refreshChart; 
	private JLabel temperature, l1, l2, l3, title;
	private String serverIP;
	private JTabbedPane tabbedPane;

	/*The constructor of the ClientGUI class
	  Gui has two different tabs:
	 -graph --> display of the sample
	 -settings --> interactions Client/Server
	 */
	public ClientGUI() 
	{
		super("Temperature Application");		
		
		do {
			Dialog D = new Dialog(this,"Server Informations", this, true);
			this.client = new Client(this.serverIP,this);
		} while (!client.isConnected());
		
		initGUI();
			
		this.setResizable(false);
		this.setSize(1000,500);
		this.setLocationRelativeTo(null);
		this.client.setClientLoop(new ClientLoop(0, this.client));
		this.setVisible(true);
	}	
	
	//Permits to initialize the different components of the GUI
	private void initGUI()
	{
		//Tab settings
			settings = new JPanel(new GridLayout(2,1));
			dashboard = new JPanel(new GridLayout(2,3));
			dashboard.setBorder(new TitledBorder("DASHBOARD"));
					
			temperature = new JLabel("Unknown");
			
			try {
				File font_file = new File("Assignment2/Font/DigitalPolice.ttf");
				Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
				temperature.setFont(font);
				Font sizedFont = font.deriveFont(45f);
				temperature.setFont(sizedFont);
			} catch (Exception e) { 
				System.out.println("Font: " + e);
			}
			temperature.setForeground(Color.red);
			temperature.setHorizontalAlignment(SwingConstants.CENTER);
			
	
			l2 = new JLabel("Temperature measured");
			l2.setHorizontalAlignment(SwingConstants.CENTER);
						
		//Set the graph
		graph = new JPanel(new BorderLayout());
		graphinput = new JPanel(new FlowLayout());
					
			title = new JLabel("Temperature Bar Chart", SwingConstants.CENTER);
			refreshChart = new JButton("REFRESH");
			refreshChart.addActionListener(this);
		
			
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(this);
	
	//Add all the different components created in a correct order
	
		//SETTINGS
		settings.add(dashboard);
		settings.add(inputs);
		
			dashboard.add(l1);
			dashboard.add(l2);
			dashboard.add(l3);
			
			dashboard.add(temperature);
			

	//The graph
	graphinput.add(refreshChart);
	graph.add(title, BorderLayout.NORTH);
	graph.add(graphinput, BorderLayout.SOUTH);
	
	//The tab
	tabbedPane.addTab("Settings", settings);
	tabbedPane.addTab("Graph", graph);
	this.getContentPane().add("Center", tabbedPane);
	
	}
	
	//Take into account all the possible changes that can be made
	public void stateChanged(ChangeEvent e) {
		
			JTabbedPane pane = (JTabbedPane) e.getSource();
			if(pane.getSelectedComponent() == graph)
			{
				if(client.getSizeHistorical()!=0)
				{
					refreshChart.setEnabled(true);
					chart = new Interface(client.getHistorical()); 
					graph.add(chart);
					
				} else {
					refreshChart.setEnabled(false);
					sendMessage("No previous sample found");
				}
			}
		}

	
	
	//ActionListener interface
	public void actionPerformed(ActionEvent e) {	
	
		if (e.getSource() == this.refreshChart)
		{
		    chart.repaint();
		}
		} 

	
	
	//Permits to display a certain value on a GUI component
	public void displayComponent(String componentSource, Requests com) throws NullPointerException
	{
		switch (componentSource)
		{
			
			case "temperature":
				this.temperature.setText(""+String.format("%.2f", com.getSample().getTemperature())+"∞C");
				break;
		}
	}
	
	//Permits or not to let the client access
	public Client getClient()
	{
		return this.client;
	}
	
	// Set the server IP
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}
	
	//Method that enable the client to send messages
	public void sendMessage(String str)
	{
		JOptionPane.showMessageDialog(this, str);
	}
	
	//Same as previous, but when there is an error
	public void sendErrorMessage(String str) {
		JOptionPane.showMessageDialog(this, str, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
    
    
   

    //The main to launch the application
	public static void main(String args[])
	{
		ClientGUI cgui = new ClientGUI();
	}
	//Permits so set correctly the Mouse interface
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}


// Method to pop-up a dialog to get the IP server address of the client
class Dialog extends JDialog implements ActionListener, MouseListener
{
		static final long serialVersionUID = 1L;
	
        private JTextField ip;
        private JTextField port;
        private JButton connect;
        private ClientGUI callingapp;

        public Dialog(Frame frame, String title, ClientGUI callingApp, boolean isModal) {
           super(frame, title, isModal);
           this.callingapp = callingApp;
           
           
           this.getContentPane().setLayout(new BorderLayout());

           JPanel main = new JPanel(new BorderLayout());
           main.setBorder(new TitledBorder("Server Informations"));
           
           JPanel top = new JPanel(new GridLayout(2,3,10,10));   
           JPanel bottom = new JPanel(new FlowLayout());
           
	           JLabel lIp = new JLabel("I.P address: ");
	           ip = new JTextField("i.e XXX.XXX.X.X",10);
	           ip.addActionListener(this);
	           ip.addMouseListener(this);
	           top.add(lIp);
	           top.add(ip);
	           
	           JLabel lPort = new JLabel("Port: ");  
	           port = new JTextField("5050",50);
	           port.addActionListener(this);
	           port.setEnabled(false);
	           top.add(lPort);
	           top.add(port);
	
	           connect = new JButton("CONNECT");
	           connect.addActionListener(this);
	           bottom.add(connect);
           
           main.add(top,BorderLayout.CENTER);
           main.add(bottom,BorderLayout.SOUTH);
           this.getContentPane().add(main);
           
           //prevent the dialog from closing without a value
           this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
           this.setResizable(false);
           this.setSize(470,230);
           this.setLocationRelativeTo(null);
           this.setVisible(true);
           
           
        }
        public void actionPerformed(ActionEvent e)
        {	
        			callingapp.setServerIP(this.ip.getText());
        			this.dispose();
        }
        public void mousePressed(MouseEvent e) {}

        public void mouseReleased(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mouseClicked(MouseEvent e) {
            if(e.getSource() == this.ip)
            	this.ip.setText("");
         }

}


