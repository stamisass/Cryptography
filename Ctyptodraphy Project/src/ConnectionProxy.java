import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
 
public class ConnectionProxy extends Thread implements StringConsumer, StringProducer
{
	
private InputStream is=null;
private OutputStream os=null;
private DataInputStream dis=null;
private DataOutputStream dos=null;
private Socket ProxySocket;
private Boolean loop=true;
private ClientGUI gui=null;
private MessageBoard mb=null;
private String tempString;
public Socket getSocket()
{
    return ProxySocket;
}
public DataInputStream getDis()
{
    return dis;
}
    public ConnectionProxy(Socket socket,ClientGUI gui) throws IOException 
    {
        is=socket.getInputStream();
        dis=new DataInputStream(is);
        os=socket.getOutputStream();
        dos=new DataOutputStream(os);
        ProxySocket=socket;
        this.gui=gui;
    }
     
    public ConnectionProxy(Socket socket,MessageBoard mb) throws IOException 
    {
        is=socket.getInputStream();
        dis=new DataInputStream(is);
        os=socket.getOutputStream();
        dos=new DataOutputStream(os);
        ProxySocket=socket;
        this.mb=mb;
    }
    public void run()
    {
    	byte[] cypher;
        while(loop)
        {
            if (gui != null)
            {
                try
                {
                    if (ProxySocket.isClosed()==false)
                    {
                        tempString=dis.readUTF();
                        System.out.println("after readUTF: "+tempString);
                        String temp = tempString;
                        String[] tokens = temp.split(": ");
                        int l = tokens.length;
                        System.out.println(l);
                        for (String t : tokens)
                        	  System.out.println(t);
                        if(l == 3){
                        if(tokens[1].equals("Pkey"))
                        {
                        	if(!tokens[0].equals(gui.tfNickName.getText()))
                        	{
                        	gui.KEY = gui.DH.getSharedSecret(new BigInteger(tokens[2].getBytes("UTF-8")));
                        	System.out.println("KEY: "+gui.KEY);
                        	System.out.println("yooooooooooo");
                        	}
                        }
                        else{
                        
                        	String temp1 = tokens[2];
                        System.out.println(tokens[0]+tokens[1]+tokens[2]);
                        	if(Integer.parseInt(tokens[1]) == 1)//rc4
                        	{
                        			   try {
                        				   
                        				   //System.out.println(tempString);
                        				   cypher = rc4.encrypt(gui.KEY.toByteArray(), temp1.getBytes());
                        			
                        				   String cyp = new String(cypher);
                        				   //System.out.println("rc4: "+cyp);
                        				   temp1 = cyp;
                        				   System.out.println("rc4 dec: "+temp1);
                        			   } catch (Exception e1) {
                        				   e1.printStackTrace();
                        			   }
                        		   }else{ if(Integer.parseInt(tokens[1]) == 2)//des3
                        		   			{
                        			
                        		   			byte[] key1 = new String(gui.KEY.toByteArray(),"UTF-8").getBytes();
                        		   			byte[] key2 = new String(gui.KEY.toByteArray(),"UTF-8").getBytes();
                        		   			byte[] key3 = new String(gui.KEY.toByteArray(),"UTF-8").getBytes();
                        			
                        		   			byte[][] keys = new byte[3][];
                        		   			keys[0]= key1;
                        		   			keys[1]= key2;
                        		   			keys[2]= key3;
                        		   			cypher = DES.TripleDES_Decrypt(temp1.getBytes(),keys);
                        			
                        		   			String cyp = new String(cypher);
                        		   			temp1 = cyp;
                        		   			System.out.println("des3 dec: "+temp1);	
                        		   			}
                        		   }
                        	tempString = tokens[0] + ": " + temp1;
                        }
                        }else if(l == 5){//signature
                        	String msg = tokens[2];
                        	if(!tokens[0].equals(gui.tfNickName.getText())){
                        	String m = tokens[3];
                        	String e = tokens[4];
                        	
                            try {
								gui.getChatBox().append(gui.sign.SignCheck(msg, m, e) + "\n");// check if the message is good
							} catch (NoSuchAlgorithmException e1) {
								e1.printStackTrace();
							}
                        	}
                        	tempString = tokens[0] + ": " + msg;
                        }
    		   				
    		   			
                     
                            if (tempString.startsWith("UserConnected-"))
                            {
                                gui.getOnlineUsers().setText("");
                                tempString=tempString.replace("UserConnected-","");
                            gui.getOnlineUsers().append(tempString+"\n");
                            }
                            else
                            {
                                gui.getChatBox().append(tempString+"\n");
                            }
                    }
                } 
                catch (IOException e) 
                {
                     
                    loop=false;
                    try {
                        ProxySocket.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                 
            }
            else
            {
                try
                {
                    if (ProxySocket.isClosed()==false)
                    {
                        tempString=dis.readUTF();  //the message that the server recieves
                        mb.broadcast(this.currentThread().getName()+": "+tempString);
                    }
                }
                 
                catch (IOException e) 
                {
                    loop=false;
                    try {
                        mb.Disconnect(this);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void consume(String str) {
        try {
        	System.out.println("before eriteUTF: "+str);
            dos.writeUTF(str);
        } catch (IOException e) 
        {
                e.printStackTrace();
        }
    }
 
 
    @Override
    public void addConsumer(StringConsumer sc) {
        // TODO Auto-generated method stub
         
    }
 
 
 
    @Override
    public void removeConsumer(StringConsumer sc) {
        // TODO Auto-generated method stub
         
    }
  
}







