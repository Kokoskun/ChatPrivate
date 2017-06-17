package TCP_Server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import TCP_Server.MessageServer;

public class Server implements ActionListener{
	public static JTextArea textArea = new JTextArea();
	public static JFrame frame = new JFrame();
	private static int portServer=-1;
	private static String ipServer="127.0.0.1";
	public static ServerSocket serverSocket;
	static MessageServer data = new MessageServer();
	static Socket connectionSocket;
	
	/*------------------------------------- Port Server -------------------------------------*/
	Server(){
		getPortServer();
	}
	
	//Get Open Port Server
		private void getPortServer(){
			try{
				while(true){
					ipServer = InetAddress.getLocalHost().getHostAddress();
					String strPortServer = JOptionPane.showInputDialog(null,"กรุณากรอกหมายเลขPortที่จะเปิดของIP Server:"+ipServer,"Port of IP Server:"+ipServer,JOptionPane.INFORMATION_MESSAGE);
					if(strPortServer.isEmpty()){
						JOptionPane.showMessageDialog(null, "คุณยังไม่ได้กรอกPortที่จะเปิดของIP Server:"+ipServer+"!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
					}else{
						try{
							portServer = Integer.parseInt(strPortServer);
							CheckPortServer();
							break;
						}catch(Exception e){
							JOptionPane.showMessageDialog(null, "คุณกรอกPortที่จะเปิดของIP Server:"+ipServer+" ผิดพลาด!!","แจ้งข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}catch(Exception e){
				if(CheckExit()==0){
					getPortServer();
				}else{
					System.out.println("ปิดServerสำเร็จ");
				}
			}
		}
		
	//Check Port Server From Client
		private void CheckPortServer(){
			try{
				if(portServer==-1){
					JOptionPane.showMessageDialog(null, "ปิดServerสำเร็จ!","แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
					System.exit(0);
				}else{
					if(ConnectServer()){
						CreateGUI();
						textArea.append("IP Server:"+ipServer+" , Port:"+portServer+" Ready to Work!\n");
					}else{
						JOptionPane.showMessageDialog(null, "Port:"+portServer+" ของIP Server:"+ipServer+" มีการใช้งานอยู่ กรุณาเลือกPortใหม่","แจ้งข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
						getPortServer();
					}
				}
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Port:"+portServer+" ของIP Server:"+ipServer+" ไม่รองรับในการใช้งาน กรุณาเลือกPortใหม่","แจ้งข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
				getPortServer();
				System.out.println("Error: Check Port Server!");
			}
		}
		/*------------------------------------- END -------------------------------------*/

	/*------------------------------------- Create GUI Server -------------------------------------*/
	private void CreateGUI(){
		frame.setTitle("IP Server:"+ipServer+" , Port:"+portServer);
		JButton btnClose = new JButton();
		btnClose.setLayout(new BorderLayout());
		btnClose.add(new JLabel("--------------------------------- Close Server ---------------------------------"));
		frame.add(btnClose,BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(new JScrollPane(textArea),BorderLayout.CENTER);
		frame.setSize(500,300);
		frame.setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus();
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnClose.addActionListener(this);
	}
	/*------------------------------------- END -------------------------------------*/
	
	/*------------------------------------- Connect Server -------------------------------------*/
	private boolean ConnectServer(){
	    try {
	    	serverSocket = new ServerSocket(portServer);
	    	return true;
	    } catch (IOException e) { 
	    	System.out.println("Error Connect Port Server");
	    	return false;
	    }
	}
	/*------------------------------------- END -------------------------------------*/
	
	//Check Exit From Server
	private int CheckExit(){
		int reply = JOptionPane.showConfirmDialog(null, "คุณต้องการปิดServer ใช่หรือไม่ ?", "ปิดServer?",  JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION){
		   return 1;
		}
		return 0;
	}

	public static void main(String[] args){
		new Server();
		while(true){
		    try{
		    	connectionSocket = serverSocket.accept();
		    	textArea.append("Accept Client: "+connectionSocket+"\n");
		    	data.ConnectSocket(connectionSocket,ipServer,portServer);
		    	data.getMessage(textArea);
		    }catch(Exception e){
		    	textArea.append("เกิดข้อผิดพลาดในการเชื่อมต่อของ: "+connectionSocket+" !! \n");
		    }
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(CheckExit()==1){
			Server.frame.setVisible(false);
			Server.frame.dispose();
			System.out.println("ปิดServerสำเร็จ");
			System.exit(0);
		}
	}

}
