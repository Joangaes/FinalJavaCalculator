package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	int port_range_init, port_range_end, port_connected, number_of_connections;
	SocketsClassifier list_of_sockets[];
	Socket socket_connection;
	Socket socket_to_self;
	ServerSocket server_socket;
	boolean server_up;
	
	public Server(int start_port, int end_port)
	{
		port_range_init = start_port;
		port_range_end = end_port;
		list_of_sockets = new SocketsClassifier[port_range_end-port_range_init];
		port_connected = connectToPortAndGetNumber(port_range_init,port_range_end);
		number_of_connections = 0;
		server_up = true;
		ConnectToOtherPorts();
		acceptConnections();
	}
	
	 

	public static void main(String []args) throws InterruptedException, IOException{
		 Server server = new Server(7001,7100);
	 }
	
	public int connectToPortAndGetNumber(int start_port, int end_port){
		for(int i = start_port; i<=end_port;i++)
		{
			try{
	             server_socket = new ServerSocket(i);
	             System.out.println("Se ha podido inicializar en el puerto : " + i);
	             return i;
	            }catch(Exception e){
	                System.out.println("No se pudo inicializar servidor en el puerto: " + i);
	            }
		}	
		return 0;
	}
	private void acceptConnections() {
		while(server_up)
		{
			try {
				//Aceptar conexion del puerto
				socket_connection = server_socket.accept();
				System.out.println("Se ha conectado el puerto:" + socket_connection.getPort());
				Listener listener_to_be_threaded = new Listener(socket_connection,this);
				Thread t = new Thread(listener_to_be_threaded);
				t.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void ConnectToOtherPorts()
	{
		for(int i = port_range_init; i<=port_range_end;i++)
		{
			if(port_connected!= i) // Avoid connecting to himself
			{
				try {
					Socket connected_socket = new Socket("localhost",i);
					System.out.println("Se ha podido conectar con:" + connected_socket.getPort());
					Listener listener_to_be_threaded = new Listener(connected_socket,this);
					Thread t = new Thread(listener_to_be_threaded);
					t.start();
				} catch (IOException e) {
					break;
				}
			}
		}
	}
	
	public synchronized void SendMessage(Socket destination_socket,Message mensaje,ObjectOutputStream outputStream)
	{
		try {
			outputStream.writeObject(mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public synchronized void AddToList(Socket socket,int type_of_connection,ObjectOutputStream outputStream)
	{
		for(int i = 0;i<number_of_connections;i++)
		{
			if(list_of_sockets[i].socket_connection == socket)
			{
				System.out.println("Se ha actualizado la conexion");
				list_of_sockets[i].type_of_conection = type_of_connection;
				return;
			}
		}
		list_of_sockets[number_of_connections] = new SocketsClassifier(socket,type_of_connection,outputStream);
		number_of_connections++;
	}
	
	public synchronized void SendMessageToCell(Message mensaje)
	{
		System.out.println("Buscando celulas para mandar mensaje");
		for(int i = 0; i<list_of_sockets.length; i++)
		{
			if(list_of_sockets[i]!=null)
			{
				if(list_of_sockets[i].type_of_conection == SocketsClassifier.CELL)
				{
					this.SendMessage(list_of_sockets[i].socket_connection, mensaje, list_of_sockets[i].outputStream);
					System.out.println("Se mando mensaje a la celula");
				}
			}
		}
	}
	
	public synchronized void SendMessageToInterface(Message mensaje) 
	{
		for(int i = 0; i<list_of_sockets.length; i++)
		{
			if(list_of_sockets[i]!=null)
			{
				if(list_of_sockets[i].type_of_conection == SocketsClassifier.INTERFACE)
				{
					System.out.println("Mandando mensaje a interfaz");
					this.SendMessage(list_of_sockets[i].socket_connection, mensaje, list_of_sockets[i].outputStream);
				}
			}
		}
	}
	
	public synchronized void ResendMessageToNodes(Message mensaje)
	{
		System.out.println("Reenviando mensaje a todos los nodos");
		for(int i = 0; i<list_of_sockets.length; i++)
		{
			if(list_of_sockets[i] != null)
			{
				if(list_of_sockets[i].type_of_conection==SocketsClassifier.NODE)
				{
					System.out.println("Reenviando a conexion:" + list_of_sockets[i].socket_connection.getPort());
					this.SendMessage(list_of_sockets[i].socket_connection, mensaje, list_of_sockets[i].outputStream);
				}
			}
		}
	}
	
}
