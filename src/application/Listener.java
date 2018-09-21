package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Listener implements Runnable {
	public Socket node_socket = null;
	public boolean listener_up = false;
	public Server master_server;
	public ObjectOutputStream outputStream;
	public ObjectInputStream inStream;
	//Type Of Connection Constants
	public static final int node = 1;
	public static final int user_interface = 2;
	public static final int cell = 3;
	//Content Code Of Messages To Be Sent
	public static final int Im_a_node = 1;
	public static final int Im_a_cell = 3;
	public static final int Im_a_user_interface=2;
	public static final int sum_operation_request = 4;
	public static final int rest_operation_request = 5;
	public static final int division_operation_request = 6;
	public static final int multiplication_operation_request = 7;
	public static final int operation_response = 8;
	public static final int print_operator = 9;
	
	public Listener(Socket node_socket, Server master_server)
	{
		this.node_socket = node_socket;
		this.master_server= master_server;
		try {
			outputStream = new ObjectOutputStream(this.node_socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Avisando que soy un nodo");
		master_server.SendMessage(this.node_socket, new Message(Im_a_node,0,0,"Nodo"), outputStream);
		this.listener_up = true;
	}
	
	public void ListenForMessages()
	{
		try {
			this.inStream = new ObjectInputStream(this.node_socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(listener_up)
		{
				Message mensaje;
				try {
					System.out.println("Esperando mensaje");
					mensaje = (Message) inStream.readObject();
					System.out.println("Llego mensaje del puerto:" + + node_socket.getPort());
					this.DoActionFromMessage(mensaje);	
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Se perdio la conexion");
					listener_up = false;
				}
				
		}
	}
	
	public void DoActionFromMessage(Message message)
	{

		switch(message.content_code)
		{
		case Im_a_node:
			master_server.AddToList(node_socket,Im_a_node,outputStream);
			System.out.println("Bienvenido nodo");
			break;
		case Im_a_cell:
			master_server.AddToList(node_socket,Im_a_cell,outputStream);
			System.out.println("Bienvenido celula");
			break;
		case Im_a_user_interface:
			master_server.AddToList(node_socket,Im_a_user_interface,outputStream);
			System.out.println("Bienvenido cliente");
			break;
		case sum_operation_request:
			System.out.println("Solicitada suma de:" + message.value_1 + " + " + message.value_2);
			if(message.resent_flag==false)
			{
				message.resent_flag = true;
				master_server.ResendMessageToNodes(message);
			}
			master_server.SendMessageToCell(message);
			break;
		case rest_operation_request:
			if(message.resent_flag==false)
			{
				message.resent_flag = true;
				master_server.ResendMessageToNodes(message);
			}
			master_server.SendMessageToCell(message);
			break;
		case multiplication_operation_request:
			if(message.resent_flag==false)
			{
				message.resent_flag = true;
				master_server.ResendMessageToNodes(message);
			}
			master_server.SendMessageToCell(message);
			break;
		case division_operation_request:
			if(message.resent_flag==false)
			{
				message.resent_flag = true;
				master_server.ResendMessageToNodes(message);
			}
			master_server.SendMessageToCell(message);
			break;
		case operation_response:
			System.out.println("Respuesta de operacion recibida");
			if(message.resent_flag==false)
			{
				message.resent_flag = true;
				master_server.ResendMessageToNodes(message);
			}
			master_server.SendMessageToInterface(message);
			break;
		case print_operator:
			System.out.println("Llego un mensaje que dice: "+message.operator);
			break;
		}
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ListenForMessages();
	}
}
