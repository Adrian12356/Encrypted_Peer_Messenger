/*
 * EPM Chat (Client Interface)
 */
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.event.*;

public class Interface implements ActionListener{
	//Construction
	public JTextArea transcriptbox;
	public JTextField sendbox;
	public JButton sendbutton;
	public JButton connectbutton;
	public JTextField ipbox;
	public JLabel iplabel;
	public JLabel portlabel;
	public JTextField portbox;
	public JLabel keylabel;
	public JTextField keybox;
	public JButton disconnect;
	public JLabel indicator;
	public JScrollPane scrollPane;
	
	//Variables
	public Boolean servup = false;
	
	//Frame
	JFrame frame;

	//Interface
	static Interface inter = new Interface();
	
	//Sockets
	BufferedReader in;
    PrintWriter out;
    
    //Server Running
    boolean isrun = false;

	public Interface() {
		//frame
		frame = new JFrame ("Encypted Peer Messenger");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

		//construct components
		transcriptbox = new JTextArea (5, 5);
		sendbox = new JTextField (5);
		sendbutton = new JButton ("Send");
		connectbutton = new JButton ("Connect");
		ipbox = new JTextField (5);
		iplabel = new JLabel ("IP");
		portlabel = new JLabel ("Port");
		portbox = new JTextField (5);
		keylabel = new JLabel ("KEY");
		keybox = new JTextField (5);
		disconnect = new JButton ("Disconnect");
		indicator = new JLabel ("Idle");
		scrollPane = new JScrollPane(transcriptbox);

		//adjust size and set layout
		frame.getContentPane().setPreferredSize (new Dimension (951, 662));
		frame.getContentPane().setLayout (null);

		//add components
		frame.getContentPane().add (scrollPane);
		frame.getContentPane().add (sendbox);
		frame.getContentPane().add (sendbutton);
		frame.getContentPane().add (connectbutton);
		frame.getContentPane().add (ipbox);
		frame.getContentPane().add (iplabel);
		frame.getContentPane().add (portlabel);
		frame.getContentPane().add (portbox);
		frame.getContentPane().add (keylabel);
		frame.getContentPane().add (keybox);
		frame.getContentPane().add (disconnect);
		frame.getContentPane().add (indicator);

		// set component bounds (only needed by Absolute Positioning)
		scrollPane.setBounds (5, 75, 940, 530);
		sendbox.setBounds (5, 605, 850, 55);
		sendbutton.setBounds (865, 610, 80, 45);
		connectbutton.setBounds (839, 20, 105, 45);
		ipbox.setBounds (25, 5, 575, 25);
		iplabel.setBounds (5, 5, 20, 25);
		portlabel.setBounds (640, 5, 45, 25);
		portbox.setBounds (680, 5, 100, 25);
		keylabel.setBounds (5, 45, 30, 25);
		keybox.setBounds (35, 45, 410, 20);
		disconnect.setBounds (490, 40, 145, 25);
		indicator.setBounds (660, 40, 145, 25);
		
		//define Indicator
		indicator.setFont(new Font("sansserif", Font.BOLD, 18));
		
		//set frame size
		frame.pack();

		//lock Frame Bounds
		frame.setResizable(false);

		//Protect Text Areas
		transcriptbox.setEditable(false);
		sendbox.setEditable(false);
		
		//disable button
		sendbutton.setEnabled(false);

		//Set Default Areas
		portbox.setText("9001");

		//Add Action Listeners
		connectbutton.addActionListener(this);
		sendbutton.addActionListener(this);
		disconnect.addActionListener(this);
		sendbox.addActionListener(this);
		
		//Set Text Wrap
		transcriptbox.setLineWrap(true);
		
		//Disable disconnect
		disconnect.setEnabled(false);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == connectbutton){
			try {
				connect();
				indicator.setText("Connecting...");
				indicator.setForeground(Color.blue);
			} catch (IOException e1) {

			}
		}else if(e.getSource() == sendbutton){
			//Show Status
			indicator.setText("Encrypting");
	        indicator.setForeground(Color.GREEN);
			
			send();
		}else if(e.getSource() == disconnect){
			try {
				disconnect();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
		}else if(e.getSource() == sendbox){
			sendbutton.doClick();
		}
	}
	private void send(){
		//Disable Connect Button
		sendbutton.setEnabled(false);
		
		//Encrypt Text
		Encryption E = new Encryption(sendbox.getText(), Keyinput);
		out.println(E.o);
        sendbox.setText("");
        
	}
	//Main
	public static void main (String[] args) {
		inter.frame.setVisible(true);
	}

	//Declare Variables
	public String Keyinput;
	public String Encrypted;
	public String Message;

	public void connect() throws IOException{
		//Encrypt Text
		Keyinput = Check.UpKey(keybox.getText());
		if (Keyinput == "error"){
			//Exit Method On Error
			return;
		}
		//enable connection
		isrun = true;
		
		//start connection
		Message = sendbox.getText().toString();
		Encryption Encryption1 = new Encryption(Message, Keyinput);
		Encrypted = Encryption1.o;
		inter.run();
		indicator.setText("Encrypting Message");
	
		//Disable Button
		connectbutton.setEnabled(false);
		keybox.setEnabled(false);
		ipbox.setEnabled(false);
		portbox.setEnabled(false);
		
		//Enable send button
		sendbutton.setEnabled(true);
		disconnect.setEnabled(true);
		sendbox.setEnabled(true);
	}
	public void disconnect() throws IOException{
		//stop server
		isrun = false;
		out.close();
		
		//disable elements
		sendbutton.setEnabled(false);
		sendbox.setEnabled(false);
		
		//enable elements
		connectbutton.setEnabled(true);
		keybox.setEnabled(true);
		ipbox.setEnabled(true);
		portbox.setEnabled(true);
		
		//clear box
		transcriptbox.setText("");
		
		//set Indicator
		indicator.setForeground(Color.BLACK);
		indicator.setText("Disconnected");
		
		//disable button
		disconnect.setEnabled(false);
		
	}
	public void run() throws IOException{
		//Connect
		String serverAddress = getServerAddress();
	    Socket socket = new Socket(serverAddress, port());
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    out = new PrintWriter(socket.getOutputStream(), true);
	    if (isrun == false){
	    	socket.close();
	    	return;
	    }
	    //Messaging
	    Runnable Threads = new Runnable(){
			public void run() {
				while (isrun == true) {
		    			String line;
						try {
							line = in.readLine();
							if (line.startsWith("SUBMITNAME")) {
				                out.println(getname());
				            } else if (line.startsWith("NAMEACCEPTED")) {
				                sendbox.setEditable(true);
				                indicator.setText("Connected!");
				            } else if (line.startsWith("ENTERPASS")){
				            	out.println(JOptionPane.showInputDialog(null, "Enter Server Password", "", JOptionPane.PLAIN_MESSAGE));
				            } else if (line.startsWith("WRONGPASS")){
				            	JOptionPane.showMessageDialog(null, "Incorrect Password");
				            	disconnect();
				            } else if (line.startsWith("PASSACCEPTED")){
				            	
				            } else if (line.startsWith("MESSAGE")) {
				                decrypt(line);
				                sendbutton.setEnabled(true);
				            } else if (isrun = false){
				            	run();
				            }
						} catch (IOException e) {
							e.printStackTrace();
						}
		        }
			}
	    	
	    };
	    new Thread(Threads).start();
	}
	Decryption D;
	public void decrypt(String line){
		//Show Status
		indicator.setText("Decrypting");
		indicator.setForeground(Color.RED);
		
		//Remove message
		String subc;
		subc = line.substring(8);
		
		//Remove sender
		String subm;
		int pos;
		pos = subc.indexOf(":") + 2;
		subm = subc.substring(pos);
		
		//decrypt Message 
		try{
			D = new Decryption(subm, Keyinput);
		}catch(Exception e){
			System.out.print(e);
			return;
		}
		
		//Recombine Components
		String message;
		String sender;
		sender = subc.substring(0, pos);
		message = sender + D.r;
		
		//Append Message
		transcriptbox.append(message + "\n");
		
		//Show Status
		indicator.setText("Idle");
		indicator.setForeground(Color.BLACK);
				
	}
	//Gets Server Port Number
	public int port() {
		int p = 9001;
		if (portbox.getText() != ""){
			try{
				p = Integer.parseInt(portbox.getText());
			}catch (Exception e){
				JOptionPane.showMessageDialog(null, "Given Port Incorrect");
			}
		}
		return p;
	}

	//Gets IP Address 
	public String getServerAddress() {
		String addr;
		addr = ipbox.getText();
		return addr;
	}
	public static String getname() {
		return JOptionPane.showInputDialog(
				null,
				"Choose a screen name:",
				"Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}
}
