package com.android.download;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MySocket{
	Socket socket = null;			//����Socket����
	DataInputStream din = null;		//������������������
	DataOutputStream dout = null;	//�����������������
	
	public MySocket(String address,int port){
		try{
			socket = new Socket(address,port);
			din = new DataInputStream(socket.getInputStream());		//���������
			dout = new DataOutputStream(socket.getOutputStream());	//��������
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//�������Ͽ����ӣ��ͷ���Դ
	public void sayBye(){
		try{
			dout.writeUTF("<#USER_LOGOUT#>");		//�����Ͽ���Ϣ
			din.close();
			dout.close();
			socket.close();
			socket=null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}