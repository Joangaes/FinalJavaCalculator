package application;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketsClassifier {
	Socket socket_connection;
	int type_of_conection;
	ObjectOutputStream outputStream;
	public static int NODE = 1;
	public static int INTERFACE = 2;
	public static int CELL = 3;
	
	public SocketsClassifier(Socket socket_to_be_added,int type_of_conection, ObjectOutputStream outputStream)
	{
		this.socket_connection = socket_to_be_added;
		this.type_of_conection = type_of_conection;
		this.outputStream = outputStream;
	}
}
