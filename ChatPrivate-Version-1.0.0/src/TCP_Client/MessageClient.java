package TCP_Client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextArea;


public class MessageClient {
	static Socket socket;
	public void StartSocket(Socket socket){
		MessageClient.socket = socket;
	}
	
	public void sendMessage(String dataMessage,JTextArea textArea,String ipServer){
		Runnable runnable = new Runnable() {
			public void run() {
				try{
					//Send String Name and Birthday
					PrintWriter dataToServer = new PrintWriter (MessageClient.socket.getOutputStream(),true);
					dataToServer.println(dataMessage);
					dataToServer.flush();
				}catch(Exception e){
					textArea.append("เกิดข้อผิดพลาดในการส่งข้อมูลไปยัง IP Server: "+ipServer+" !!\n");
				}
			}
		};
		new Thread(runnable).start();
	}


	public boolean getMessage(JTextArea textArea, String ipServer) {
		try{
			//Get Data From Server
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String dataFromServer = fromServer.readLine();
			if(dataFromServer != null){
				textArea.setDisabledTextColor(Color.black);
				textArea.append(dataFromServer);
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
