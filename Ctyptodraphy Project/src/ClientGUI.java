import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;










import javax.swing.*;

import java.math.BigInteger;

public class ClientGUI implements StringConsumer, StringProducer
{
	public static Signature sign = new Signature();
	public static String kk = "LUYGVFRAMTIFBCNEFHTEWXH7JMVFESOP";
	public static BigInteger KEY = new BigInteger(kk.getBytes());
	int zero = 0;;
	String Security=null;
	String KeyExxx = null;
	String signa = "NO";
	private JPanel North, South, Center;
	private JFrame frame;
	private JButton btConnect;
	private JButton btDisconect;
	private JButton btSend;
	private JLabel labelNickname;
	private JLabel labelKeyEx;
	private JLabel labelPKey;
	private JLabel labelSecur;
	private JLabel labelSign;
	private JLabel labelMessage;
	private JLabel labelPort;
	private JLabel labelIp;
	private JLabel labelConnectedUsers;
	public JTextField tfIp, tfPort, tfUserMessage,tfNickName, PKey;
	private  JTextArea chatBox;
	private JTextArea OnlineUsers;
	private Socket socket=null;
	public ConnectionProxy connection = null;
	private JScrollPane sbChatBox;
	private JScrollPane sbOnlineUsers;
	public static JComboBox<String> securType;
	public static JComboBox<String> KeyExc;
	public static JComboBox<String> Siggn;
	public DiffieHellman DH;
	
	
	public JTextArea getChatBox() {
		return chatBox;
	}
	public void setChatBox(JTextArea chatBox) {
		this.chatBox = chatBox;
	}
	public JTextArea getOnlineUsers() {
		return OnlineUsers;
	}
	public void setOnlineUsers(JTextArea onlineUsers) {
		OnlineUsers = onlineUsers;
	}
	public void connect() throws IOException
	{
		connection= new ConnectionProxy(socket,this);			//set Server side proxy's Name = User`s Nickname
		chatBox.append("Welcome to the chat!"+"\n");
		connection.start();
		connection.setName("ClientSideProxy");
		connection.consume(tfNickName.getText());
	}
	public ClientGUI()
	{
		
		North=new JPanel();
		South=new JPanel();
		Center=new JPanel();
		frame=new JFrame("Java Chat by Tamir Abu Salah");
		btConnect=new JButton("Connect");
		btDisconect=new JButton("Disconnect");
		btDisconect.setEnabled(false);
		btSend=new JButton("Send");
		btSend.setEnabled(false);
		labelNickname=new JLabel("NickName:");
		labelKeyEx=new JLabel("KeyEx:");
		labelPKey=new JLabel("PKey:");
		labelSign=new JLabel("Signature:");
		labelMessage=new JLabel("Message:");
		labelConnectedUsers=new JLabel("Online users;");
		labelIp=new JLabel("Ip:");
		labelPort=new JLabel("Port:");
		labelSecur = new JLabel("Increption mode:") ;
		tfIp=new JTextField("10.0.0.4");	//16 chars long
		PKey = new JTextField("1");
		tfPort=new JTextField("1500"); 	//5 chars long 
		tfNickName=new JTextField(10);	//10 chars long
		tfUserMessage=new JTextField(40);  //40 chars long
		tfUserMessage.setEditable(false);
		chatBox=new JTextArea(27,70);
		chatBox.setEditable(false);
		OnlineUsers=new JTextArea(27,14);
		OnlineUsers.setEditable(false);
		Center.setBackground(Color.DARK_GRAY);
		chatBox.setLineWrap(true);
		frame.getRootPane().setDefaultButton(btSend);
		sbChatBox=new JScrollPane(chatBox);
		sbOnlineUsers=new JScrollPane(OnlineUsers);
		chatBox.setLineWrap(true);
		String [] secu = new String[3];
		secu[0] ="NONE";
		secu[1]= "RC4";
		secu[2]= "DES3";
		securType = new JComboBox<String>(secu);
		String [] KeyExx = new String[2];
		KeyExx[0] ="NONE";
		KeyExx[1] = "DH";
		KeyExc = new JComboBox<String>(KeyExx);
		String [] Signn = new String[2];
		Signn[0] ="NO";
		Signn[1] = "YES";
		Siggn = new JComboBox<String>(Signn);

	}
	class ConnectbtListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String Nickname=null;
			String ip=null;
			int p = 17;
			int g = 3;
			BigInteger pp = BigInteger.valueOf(p);
			BigInteger gg = BigInteger.valueOf(g);
			DH = DH.getDefault();

			int Port = 0;
			
			Security = (String) securType.getSelectedItem();
			KeyExxx = (String) KeyExc.getSelectedItem();
			Nickname=tfNickName.getText();
			ip=tfIp.getText();
			Port=Integer.parseInt(tfPort.getText());
			if((tfNickName.getText()).equals("") || (tfIp.getText()).equals("") || (tfPort.getText()).equals("") )
			{
				
				chatBox.append("Error: Make sure your input is correct..."+"\n");
			}
			
			else
			{
			
			try
			{
				socket=new Socket(ip,Port);
				chatBox.append("Connecting.."+"\n");
			}
			 
			catch (Exception e2) 
			{
				chatBox.append("Error: The server you are trying to connect to is offline.."+"\n");
				e2.printStackTrace();
			}

			try
			{
				connect();
				chatBox.append("KeyEchange: " + KeyExxx + "\n");
				DH = DiffieHellman.recreate(new BigInteger(PKey.getText().getBytes()), DiffieHellman.DEFAULT_MODULUS);
				if(KeyExxx == "DH")
	     		{
					String msgg = "Pkey: "+ DH.getPublicKey().toString();
					connection.consume(msgg);
					msgg = "";
				}
			chatBox.append("THE SECURITY TYPE IS: " + Security+ "\n");
		
			} 
			catch (IOException e1)
			{
				System.out.println("Error connectin to the server...try again");
				e1.printStackTrace();
			}
			
			tfUserMessage.setEditable(true);
			btConnect.setEnabled(false);
			btDisconect.setEnabled(true);
			btSend.setEnabled(true);
			}
		}		
	}
	
	class disconnectBt implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				socket.close();
				connection=null;
				chatBox.append("Disconnecting.."+"\n");
			} catch (IOException e1) {
				chatBox.append("You are already disconnected."+"\n");
				e1.printStackTrace();
			}
			if(socket.isClosed())
			{
			chatBox.append("Disconnected"+"\n");
			tfUserMessage.setEditable(false);
			btConnect.setEnabled(true);
			btDisconect.setEnabled(false);
			btSend.setEnabled(false);
			OnlineUsers.setText("");
			tfUserMessage.setText("");
			
			}
			
		}		
	}
	
	class sendListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			byte[] cypher;
			String msg = tfUserMessage.getText();
			String msgg = "0: "+msg;
			if (!(tfUserMessage).getText().equals(null) || !(tfUserMessage).getText().equals(""))
			{	
				System.out.println("selected securtyType: "+securType.getSelectedItem());
				}
				if(securType.getSelectedItem().equals("RC4"))
				{
					
					System.out.println("KEYy: "+KEY);
				  try
				   {
					
					//System.out.println(tempString);
					cypher = rc4.encrypt(KEY.toByteArray(),tfUserMessage.getText().getBytes());
					String cyp = new String(cypher);
					msg = cyp;
					msgg = "1: "+msg; 
					System.out.println("rc4 befor consume: "+msgg);
				   } catch (Exception e1) {
					   e1.printStackTrace();
				      }
				}else{ if(securType.getSelectedItem().equals("DES3"))
				       {
						
						 byte[][] keys = new byte[3][];
						 try {
						byte[] key1 = new String(KEY.toByteArray(),"UTF-8").getBytes();
     		   			byte[] key2 = new String(KEY.toByteArray(),"UTF-8").getBytes();
     		   			byte[] key3 = new String(KEY.toByteArray(),"UTF-8").getBytes();
     		   			keys[0]= key1;
     		   			keys[1]= key2;
     		   			keys[2]= key3;
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					    
					     cypher = DES.TripleDES_Encrypt(tfUserMessage.getText().getBytes(),keys);
					     String cyp = new String(cypher);
					     msg = cyp;
					     msgg = "2: "+msg; 
					     System.out.println("des3 befor consume: "+msgg);
				       }else{ if(Siggn.getSelectedItem().equals("YES"))
				       	{
				    	   String m = null;
				    	   try {
				    		System.out.println("before hash: "+msg);   
							m = sign.getHash(msg);
							System.out.println("after hash: " +m);
							System.out.println("after hash: " +m.getBytes());
				    	   } catch (NoSuchAlgorithmException
								| UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
				    	   }
				    	   
				    	   try {
				    	    m = sign.Sign(m);
							msgg = "3: " + msg + ": " + m + ": " + sign.GetE();
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				       	}
				       	  
				       
				       }
					
				
				}
				System.out.println("befor consume: "+msgg);
				connection.consume(msgg);
				tfUserMessage.setText("");
			}						
		}
	

	public void start()
	{	
		ActionListener connectListener= new ConnectbtListener();
		btConnect.addActionListener(connectListener);
		ActionListener disconnectListener=new disconnectBt();
		btDisconect.addActionListener(disconnectListener);
		ActionListener sendListener= new sendListener();
		btSend.addActionListener(sendListener);
		frame.setLayout(new BorderLayout());
		North.add(labelNickname);
		North.add(tfNickName);
		North.add(labelSecur);
		North.add(securType);
		North.add(labelKeyEx);
		North.add(KeyExc);
		North.add(labelPKey);
		North.add(PKey);
		North.add(labelSign);
		North.add(Siggn);
		North.add(labelIp);
		North.add(tfIp);
		North.add(labelPort);
		North.add(tfPort);
		North.add(btConnect);
		North.add(btDisconect);
		Center.add(sbChatBox);
		Center.add(sbOnlineUsers);
		South.add(labelMessage);
		South.add(tfUserMessage);
		South.add(btSend);
		frame.add(North, BorderLayout.NORTH);
		frame.add(Center, BorderLayout.CENTER);
		frame.add(South, BorderLayout.SOUTH);
		frame.setSize(1000, 600);
		frame.setVisible(true);
		
		sbChatBox.getVerticalScrollBar().addAdjustmentListener(	//JScrollpane - Force autoscroll to bottom
				new AdjustmentListener() {
					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						chatBox.select(chatBox.getHeight() +1000, 0);
						
					}
				});
		frame.addWindowListener(new WindowAdapter() 		//window closing listener
		{
			public void windowClosing(WindowEvent event)
			{
				frame.setVisible(false);
				frame.dispose();
				System.exit(0);
			}
		});
		
	}
	public static void main(String[] args) 
	{

				ClientGUI gui = new ClientGUI();
				gui.start();

	}
	@Override
	public void addConsumer(StringConsumer sc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeConsumer(StringConsumer sc) {
		// TODO Auto-generated method stub
		
	}
//	@Overrider
	public void consume(String str) {
		
		
		
	}
}

