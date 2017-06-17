package TCP_Client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;
import TCP_Client.MessageClient;
public class Client implements ActionListener{
	private static JTextField textSend= new JTextField();
	private static JTextArea textArea = new JTextArea();
	private static String nameUser=null;
	private static String ipUser=null;
	private static String ipServer=null;
	private static String portServer=null;
	private static Socket socket;
	static MessageClient data = new MessageClient();
	DefaultCaret caret;
	
	private Client(){
		getIPServer();
		CheckIPServer();
	}
	
	/*------------------------------------- Get and Check IP Server -------------------------------------*/
	//Get IP Server From Client
	private void getIPServer(){
		try{
			while(true){
				ipServer = JOptionPane.showInputDialog(null,"กรุณากรอกIPของServer","IP Server",JOptionPane.INFORMATION_MESSAGE);
				if(ipServer.isEmpty()){
					JOptionPane.showMessageDialog(null, "คุณยังไม่ได้กรอกIPของServer!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
				}else{
					break;
				}
			}
		}catch(Exception e){
			if(CheckExit()==0){
				getIPServer();
			}else{
				System.out.println("ออกจากระบบสำเร็จ");
			}
		}
	}
	
	//Check IP Server From Client
	private void CheckIPServer(){
		try{
			if(ipServer==null){
				JOptionPane.showMessageDialog(null, "ออกจากระบบสำเร็จ!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}else{
				getPortServer();
				CheckPortServer();
			}
		}catch(Exception e){
			System.out.println("Error: Check IP Server!");
		}
	}
	
	//Get Port Server From Client
	private void getPortServer(){
		try{
			while(true){
				portServer = JOptionPane.showInputDialog(null,"กรุณากรอกPortของ IP Server:"+ipServer,"Port of IP Server:"+ipServer,JOptionPane.INFORMATION_MESSAGE);
				if(portServer.isEmpty()){
					JOptionPane.showMessageDialog(null, "คุณยังไม่ได้กรอกPortของIP Server:"+ipServer+"!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
				}else{
					try{
						Integer.parseInt(portServer);
						break;
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, "คุณกรอกPortของIP Server:"+ipServer+" ผิดพลาด!!","แจ้งข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}catch(Exception e){
			if(CheckExit()==0){
				getPortServer();
			}else{
				System.out.println("ออกจากระบบสำเร็จ");
			}
		}
	}
	
	//Check Port Server From Client
	private void CheckPortServer(){
		try{
			if(portServer==null){
				JOptionPane.showMessageDialog(null, "ออกจากระบบสำเร็จ!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}else{
				getName();
				CheckName();
			}
		}catch(Exception e){
			System.out.println("Error: Check Port Server!");
		}
	}
	/*------------------------------------- END -------------------------------------*/

	/*------------------------------------- Get and Check Name -------------------------------------*/
	//Get Name From Client
	private void getName(){
		try{
			while(true){
				ipUser = InetAddress.getLocalHost().getHostAddress();
				nameUser = JOptionPane.showInputDialog(null,"IP Server:"+ipServer+"\n Port:"+portServer+"\n IP Me:"+ipUser+"\n กรุณากรอกชื่อผู้ใช้งาน","ชื่อผู้ใช้งานใน"+"IP Server:"+ipServer+", Port:"+portServer,JOptionPane.INFORMATION_MESSAGE);
				if(nameUser.isEmpty()){
					JOptionPane.showMessageDialog(null, "คุณยังไม่ได้กรอกชื่อผู้ใช้งาน!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
				}else{
					break;
				}
			}
		}catch(Exception e){
			if(CheckExit()==0){
				getName();
			}else{
				System.out.println("ออกจากระบบสำเร็จ");
			}
		}
	}
	
	//Check Name From Client
	private void CheckName(){
		try{
			if(nameUser==null){
				JOptionPane.showMessageDialog(null, "ออกจากระบบสำเร็จ!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}else{
				ConnectServer();
			}
		}catch(Exception e){
			System.out.println("Error: Check Name!");
		}
	}
	/*------------------------------------- END -------------------------------------*/

	/*------------------------------------- Connect Server -------------------------------------*/
	private void ConnectServer(){
		try{
			Client.socket = new Socket(ipServer, Integer.parseInt(portServer));
			JOptionPane.showMessageDialog(null, "ยินดีต้อนรับ คุณ"+nameUser+" , IP:"+ipUser,"การเข้าสู่ระบบสำเร็จ", JOptionPane.INFORMATION_MESSAGE);
			CreateGUI();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "การเชื่มอต่อ IP Server:"+ipServer+" , Port:"+portServer+" ล้มเหลว!!","การเชื่อมต่อล้มเหลว", JOptionPane.ERROR_MESSAGE);
			new Client();
		}
	}
	/*------------------------------------- END -------------------------------------*/

	/*------------------------------------- GUI -------------------------------------*/
	//Create GUI
	private void CreateGUI(){
		JPanel panel = new JPanel();
		JFrame frame = new JFrame("User:"+nameUser+" , IP:"+ipUser+" To IP Server:"+ipServer+" , Port:"+portServer);
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(nameUser+": "),BorderLayout.WEST);
		panel.add(textSend,BorderLayout.CENTER);
		frame.add(panel,BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(new JScrollPane(textArea),BorderLayout.CENTER);
		frame.setSize(500,500);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus();
		frame.setFont(new Font("Tahoma", Font.PLAIN, 14));
		caret=(DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textSend.addActionListener(this);
		textSend.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
	}
	/*------------------------------------- END -------------------------------------*/
	
	//Check Exit From Client
	private int CheckExit(){
		int reply = JOptionPane.showConfirmDialog(null, "คุณต้องการปิดระบบ ใช่หรือไม่ ?", "ปิดโปรแกรม?",  JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION){
		   return 1;
		}
		return 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JTextField){
			String dataMessage = null;
			String message =textSend.getText();
			if(message.isEmpty()){
				JOptionPane.showMessageDialog(null, "คุณยังไม่กรอกข้อความ!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
			}else{
				try{
					dataMessage = nameUser+" ( "+ipUser+" )"+": "+message;
					data.sendMessage(dataMessage,textArea,ipServer);
					textSend.setText(null);
				}catch(Exception error){
					textArea.append("ผิดพลาดในการเชื่อมต่อของIP Server: "+ipServer+"\n");
				}
			}
		}
	}
	public static void main(String[] args){
		new Client();
		data.StartSocket(socket);
		data.sendMessage(nameUser+" ("+ipUser+") ",textArea,ipServer);
		textArea.append("เชื่อมต่อ "+ipServer+" เรียบร้อย\n");
		while(true){
		    try{
		    	if(data.getMessage(textArea,ipServer)){
		    		textArea.append("\n");
		    	}else{
		    		socket.close();
			    	JOptionPane.showMessageDialog(null, "ไม่สามารถรับข้อมูลในการติดต่อของ IP Server: "+ipServer+" ได้!!","ข้อผิดพลาดในการเชื่อมต่อ", JOptionPane.ERROR_MESSAGE);
					textArea.append("การเชื่อมต่อ IP Server:"+ipServer+" ผิดพลาดกรุณาเข้าสู่ระบบใหม่\n");
			    	break;
		    	}
		    }catch(Exception e){
		    	JOptionPane.showMessageDialog(null, "ไม่สามารถรับข้อมูลในการติดต่อของ IP Server: "+ipServer+" ได้!!","ข้อผิดพลาดในการเชื่อมต่อ", JOptionPane.ERROR_MESSAGE);
		    }
		}
	}

}
