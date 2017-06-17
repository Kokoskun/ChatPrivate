package TCP_Server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextArea;

public class MessageServer {
	
	static Socket connectionSocket;
	static String ipServer;
	private static Socket[] socketClient = new Socket[100];
	private static int countClient =0;
	private static int portServer;
	private void setTitleServer(){
		Server.frame.setTitle("IP Server:"+ipServer+" , Port:"+portServer+" Count Client:"+countClient);
	}
	public void ConnectSocket(Socket connectionSocket,String ipServer,int portServer){
		MessageServer.connectionSocket = connectionSocket;
		MessageServer.ipServer = ipServer;
		MessageServer.socketClient[countClient] = connectionSocket;
		MessageServer.portServer = portServer;
		MessageServer.countClient+=1;
		setTitleServer();
	}

	public void getMessage(JTextArea textArea){
		Runnable runnable = new Runnable() {
			public void run() {
				Socket socketUser =connectionSocket;
				String infoUser =infoClient(socketUser);
				sendMessage(textArea,infoUser+" เข้าสู่ระบบ...");
				String dataFromClient;
				try{
					while(true){
						BufferedReader fromClient = new BufferedReader(new InputStreamReader(socketUser.getInputStream()));
						//หลังผู้ใช้งานเข้าสู่ระบบ จะรอข้อมูลส่งนำเข้ามา
						dataFromClient = fromClient.readLine();
						if(dataFromClient != null){
							sendMessage(textArea,dataFromClient);
						}
					}
				}catch(Exception e){
					try {
						textArea.append("Client Exit: "+socketUser+" !! \n");
						disconnectUser(socketUser);
						sendMessage(textArea,infoUser+" ออกจากระบบ!!");
						setTitleServer();
						socketUser.close();
					} catch (Exception e1) {
						textArea.append("เกิดข้อผิดพลาดในการออกจากระบบของ: "+socketUser+" !! \n");
					}
				}
			}
		};
		new Thread(runnable).start();
	}
	private String infoClient(Socket socketUser){
		try{
			String dataInfoClient;
			BufferedReader infoClient = new BufferedReader(new InputStreamReader(socketUser.getInputStream()));
			dataInfoClient = infoClient.readLine();
			return dataInfoClient;
		} catch (Exception e1) {
			return "Anonymous";
		}
	}
	private void disconnectUser(Socket socketUser){
		int countUser =MessageServer.countClient;
		MessageServer.countClient-=1;
		for(int item=0;item<countUser;item++){
			if(socketUser==MessageServer.socketClient[item] && MessageServer.socketClient[item]==null){
				if(MessageServer.socketClient[item+1]!=null){
					MessageServer.socketClient[item]=MessageServer.socketClient[item+1];
					MessageServer.socketClient[item+1]=null;
				}else{
					MessageServer.socketClient[item]=null;
				}
			}
		}
	}
	public void sendMessage(JTextArea textArea,String dataFromClient){
		Runnable runnable = new Runnable() {
			public void run() {
				for(int item=0;item<MessageServer.countClient;item++){
					try{
						//Send Data To Client
						PrintWriter dataToClient = new PrintWriter (MessageServer.socketClient[item].getOutputStream(),true);
						dataToClient.println (dataFromClient);
						dataToClient.flush();
					}catch(Exception e){
						textArea.setDisabledTextColor(Color.red);
						textArea.append("ข้อผิดพลาดในการส่งข้อมูล "+socketClient[item]+" !! \n");
					}	
				}
			}
		};
		new Thread(runnable).start();
	}
}
